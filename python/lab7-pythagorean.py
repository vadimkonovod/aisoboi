def pythagorean(n):
	return [(x,y,z) for x in range(1,n) for y in range(x,n) for z in range(y,n) if x**2 + y**2 == z**2]