#!/usr/bin/python

import sys

def collatz(n):
    n = int(n)
    steps = [n]
    while(n != 1):
        if (n % 2 == 0):
            n = n / 2
        else:
            n = 3 * n + 1
        steps.append(n)
    print(" -> ".join(map(str, steps)))

def validate(n):
    if not n.isdigit():
        print("error: please enter a natural number")
        return False
    elif int(n) < 1:
        print("error: number should be greater than 0")
        return False
    else:
        return True

n = sys.argv[1]
if (validate(n)):
    collatz(n)