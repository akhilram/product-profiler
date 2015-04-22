import json as simplejson
import sys

def parse(filename):
  f = open(filename, 'r')
  entry = {}
  for l in f:
    l = l.strip()
    colonPos = l.find(':')
    if colonPos == -1:
      yield entry
      entry = {}
      continue
    eName = l[:colonPos]
    rest = l[colonPos+2:]
    if eName == "review/text":
        entry[eName] = rest
  yield entry

option = "singlefile"
# option = "multiplefile"

if option == "singlefile":
    out = open('ReviewInputB000GAYQMM.txt', 'w')
    for e in parse("B000GAYQMM.txt"):
        out.write(e["review/text"])
        out.write("\n")
        print simplejson.dumps(e)
    out.close()

else:
    count = 1;
    for e in parse("B000GAYQL8.txt"):
        out = open('reviews/ReviewInput' + str(count) + '.txt', 'w')
        out.write(e["review/text"])
        out.write("\n")
        count += 1
        print simplejson.dumps(e)
    out.close()