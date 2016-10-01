def cumsum(mylist):
    return [sum(mylist[:i]) for i in range(len(mylist) + 1)] 

def cumsum(mylist):
    l = [0];
    for x in mylist:
        l.append(x + l[-1])
    return l