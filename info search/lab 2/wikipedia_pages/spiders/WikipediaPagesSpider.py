# coding: utf-8
from __future__ import unicode_literals

from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor

from wikipedia_pages.items import WikipediaItem
import re


class WikipediaPagesSpider(CrawlSpider):
    name = 'wikipedia_pages'

    start_urls = ['https://en.wikipedia.org/wiki/Minsk',
                  'https://en.wikipedia.org/wiki/Python_(programming_language)']

    links_xpath = '(//div[@id="mw-content-text"]/div/p/a)[position()<100]'
    allow_re = '/wiki/' \
               '(?!((File|Talk|Category|Portal|Special|' \
               'Template|Template_talk|Wikipedia|Help|Draft):|Main_Page)).+'
    compiled_allow_re = re.compile('/wiki/'
                                   '(?!((File|Talk|Category|Portal|Special|'
                                   'Template|Template_talk|Wikipedia|Help|Draft):|Main_Page)).+')
    snippet_xpath = 'string(//div[@id="bodyContent"]/div/p[1])'

    rules = (
        Rule(LinkExtractor(restrict_xpaths=links_xpath,
                           deny='#.*',
                           allow=allow_re),
             callback='parse_item', follow=True),
    )

    def parse_start_url(self, response):
        return self.parse_item(response)

    def parse_item(self, response):
        item = WikipediaItem()
        item['url'] = response.url
        item['links'] = [response.urljoin(link) for link in response.xpath(self.links_xpath).xpath('@href').extract()
                         if self.compiled_allow_re.match(link)]
        item['snippet'] = response.xpath(self.snippet_xpath).extract_first()[:255] + '...'
        return item