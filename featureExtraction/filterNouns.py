import re

ifile = open('ReviewTagged.txt', 'r')
ofile = open('ReviewFiltered.txt', 'w')
line = ifile.readline()

r = '^([a-z]+)([A-Z]+)'

while line:
    tokens = line.split(' ')
    for token in tokens:
        if token.endswith('NN') or token.endswith('NNP'):
            m = re.search(r, token)
            if m:
                ofile.write(m.group(1) + ' ')
    line = ifile.readline()
    ofile.write('\n')