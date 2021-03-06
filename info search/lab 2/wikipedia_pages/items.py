# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

from __future__ import unicode_literals

import scrapy


class WikipediaItem(scrapy.Item):
    id = scrapy.Field()
    url = scrapy.Field()
    links = scrapy.Field()
    snippet = scrapy.Field()