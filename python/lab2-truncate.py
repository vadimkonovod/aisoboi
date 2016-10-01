def truncate(list, a, b):
    return [max(a, min(b, x)) for x in list]