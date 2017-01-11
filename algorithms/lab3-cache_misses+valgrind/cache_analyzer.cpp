#include <vector>
#include <iostream>
#include <fstream>
#include <random>
#include <map>
#include <ctime>
#include <chrono>
#include <functional>

using ui32 = unsigned int;
using ui64 = unsigned long int;

struct CacheInfo {
    ui32 lineSize;
    ui32 linesInBlock;
    ui32 associativity;
};

struct CacheCell {
    ui32 writes = 0;
    ui32 reads = 0;

    struct Record {
        std::size_t addr;
        ui32 lastRead;
        ui32 lastWrite;
    };
    std::vector<Record> linesIn;
    std::map<std::size_t, ui32> linesOut;
};

struct PopStrategyLastUsage {

    PopStrategyLastUsage(ui32) {}

    ui32 pop(const CacheCell& cacheCell) {
        auto lu = 0;
        for (ui32 i = 1; i < cacheCell.linesIn.size(); ++i) {
            if (cacheCell.linesIn[i].lastRead < cacheCell.linesIn[lu].lastRead)
                lu = i;
        }
        return lu;
    }

    std::string name() const { return "LastUsage"; }
};

struct PopStrategyRandom {

    PopStrategyRandom(ui32 associativity)
        : engine(std::chrono::system_clock::now().time_since_epoch().count())
        , distribution(0, associativity - 1)
    {
    }

    ui32 pop(const CacheCell&) {
        return distribution(engine);
    }

    std::string name() const { return "Random"; }

    std::default_random_engine engine;
    std::uniform_int_distribution<ui32> distribution;

};

struct Statistics {
    ui64 hits = 0;
    ui64 misses = 0;

    void updateUsagesIn(ui32 dur) {
        if (dur >= lastUsageIn.size())
            lastUsageIn.resize(dur + 1, 0);
        ++lastUsageIn[dur];
    }
    void updateUsagesOut(ui32 dur) {
        if (dur >= lastUsageOut.size())
            lastUsageOut.resize(dur + 1, 0);
        ++lastUsageOut[dur];
    }
    std::vector<ui64> lastUsageIn;    // [LastUsageDate] : count
    std::vector<ui64> lastUsageOut;   // [LastUsageDate] : count
};

template <typename Strategy = PopStrategyRandom>
class Cache {
public:
    explicit Cache(const CacheInfo& info)
        : info(info)
        , popStrategy(info.associativity)
    {
        block.resize(info.linesInBlock);
    }

    template <typename T>
    void use(T* addr) {
        auto lineaddr = reinterpret_cast<std::size_t>(addr) / info.lineSize;
        ui32 celladdr = lineaddr % info.linesInBlock;
        auto & cell = block[celladdr];

        for (auto & r : cell.linesIn)
            if (r.addr == lineaddr) {
                r.lastRead = cell.reads++;
                ++stat.hits;
                stat.updateUsagesIn(cell.writes - r.lastWrite - 1);
                return;
            }

        ++stat.misses;

        if (cell.linesIn.size() < info.associativity) {
            cell.linesIn.emplace_back(
                CacheCell::Record{lineaddr, cell.reads++, cell.writes++}
            );
            return;
        }

        auto i = cell.linesOut.find(lineaddr);
        if (i != std::end(cell.linesOut)) {
            stat.updateUsagesOut(cell.writes - i->second - 1);
        }

        auto & r = cell.linesIn[ popStrategy.pop(cell) ];
        cell.linesOut[r.addr] = r.lastWrite;
        r.addr = lineaddr;
        r.lastRead = cell.reads++;
        r.lastWrite = cell.writes++;
    }

    const Statistics& statistics() const { return stat; }
    const Strategy & strategy() const { return popStrategy; }
    const CacheInfo & cacheInfo() const { return info; }

private:
    const CacheInfo info;
    Strategy popStrategy;
    std::vector<CacheCell> block;
    Statistics stat;
};


template <typename Cache>
using MultFunction = std::function<void (Cache&)>;

template <typename Cache>
Cache Test(MultFunction<Cache> f, const CacheInfo& info) {
    using namespace std::chrono;

    Cache cache(info);

    auto startTime = system_clock::now();
    f(cache);
    std::cerr << duration_cast<seconds>(system_clock::now() - startTime).count() << " sec\n";

    auto stat = cache.statistics();
    std::cout << cache.strategy().name()
              << " hits: " << stat.hits
              << ", misses: " << stat.misses
              << ", all: " << (stat.hits + stat.misses) << std::endl;
    return cache;
}

template <typename Cache>
inline std::ofstream& Write(std::ofstream& ofs, const Cache& cache, const std::string& funcName) {
    ofs << funcName << std::endl;

    const auto & stat = cache.statistics();
    ofs << cache.strategy().name() << " " << "misses: " << stat.misses << " all: "
        << (stat.misses + stat.hits) << std::endl;

    for (auto luin : stat.lastUsageIn)
        ofs << luin << " ";
    ofs << std::endl;
    for (auto luout : stat.lastUsageOut)
        ofs << luout << " ";
    ofs << std::endl;

    return ofs;
}
