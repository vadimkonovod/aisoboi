def reverse(str):
    p = re.compile('[a-zA-Z]+')
    words = p.findall(str)
    print p.sub(lambda match: words[-(1 + words.index(match.group()))], str)