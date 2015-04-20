import operator

def getTopics():
    topics = {}
    keyFile = open('filtered.keys', 'r')
    line = keyFile.readline()
    while line:
        tokens = line.split()
        for token in tokens:
            if str.isalpha(token):
                topics[str(token)] = 0
        line = keyFile.readline()
    return topics

def updateFrequency(topics):
    reviewFile = open('ReviewFiltered.txt', 'r')
    line = reviewFile.readline()
    while line:
        tokens = line.split(' ')
        for token in tokens:
            if topics.get(token) is not None:
                topics[token] += 1
        line = reviewFile.readline()
    return topics

features = getTopics()
features = updateFrequency(features)
sorted_features = sorted(features.items(), key=operator.itemgetter(1), reverse=True)

for key in sorted_features:
    print(key[0])

print(sorted_features)