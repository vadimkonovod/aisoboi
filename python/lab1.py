def cum(list):
    return [sum(list[:i]) for i in range(len(list) + 1)] 

def cum(list):
    l = [0];
    for x in list:
        l.append(x + l[-1])
    return l