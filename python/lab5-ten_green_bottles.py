numbers = ["Ten", "Nine", "Eight", "Seven", "Six", "Five", "Four", "Three", "Two", "One", "No"]
gb = "green bottle"
gbs = "{}s".format(gb)
main = "{} hanging on the wall"
first_line = "{0} {1},\n" 
all_lines = "{0}{0}And if one {1} should accidentally fall,\nThere'll be {2} {3}." 
for i in range(len(numbers)-1):
   gb_first = gb if numbers[i] == "One" else gbs;
   gb_last = gb if numbers[i] == "Two" else gbs;
   print all_lines.format(first_line.format(numbers[i], main.format(gb_first)), gb, numbers[i+1].lower(), main.format(gb_last))