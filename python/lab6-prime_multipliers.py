from collections import Counter

def factor(n):
    ans = []
    d = 2
    while d * d <= n:
        if n % d == 0:
            ans.append(d)
            n //= d
        else:
            d += 1
    if n > 1:
        ans.append(n)
    counter = Counter(ans)
    return sorted(counter.items(), key=lambda x: x[0])
    
def tally(c):
	t = dict()
	for x in c:
		t[x] = t.get(x, 0) + 1
	return sorted(t.items(), key=lambda x: x[0])

print factor(12)