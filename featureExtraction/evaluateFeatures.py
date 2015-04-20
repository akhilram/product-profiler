import json
from pprint import pprint

def getGoldFeatureSet():
    featureSet = {}
    with open('B000GAYQMM.json') as data_file:
        data = json.load(data_file)
    for review in data['items']:
        for features in review['features']:
            if ' ' not in features:
                featureSet[features.encode("ascii").lower()] = 1
    return featureSet

def getOutputFeatureSet():
    features = open('Features.txt', 'r')
    featureSet = {}
    line = features.readline()
    while line:
        featureSet[line.rstrip('\n')] = 1
        line = features.readline()

    return featureSet

def calculatePrecision(outputFeatureSet, goldFeatureSet):
    correct = 0
    for feature in outputFeatureSet:
        if goldFeatureSet.get(feature):
            correct += 1
    length = len(outputFeatureSet)
    precision = float(correct)/length
    return precision

def calculateRecall(outputFeatureSet, goldFeatureSet):
    correct = 0
    for feature in goldFeatureSet:
        if outputFeatureSet.get(feature):
            correct += 1
    length = len(goldFeatureSet)
    recall = float(correct)/length
    return recall

def calculateFscore(precision, recall):
    return (2*precision*recall)/(precision+recall)

goldFeatureSet = getGoldFeatureSet()
print(goldFeatureSet)

outputFeatureSet = getOutputFeatureSet()
print(outputFeatureSet)

precision = calculatePrecision(outputFeatureSet, goldFeatureSet)
print(precision)

recall = calculateRecall(outputFeatureSet, goldFeatureSet)
print(recall)

fscore = calculateFscore(precision, recall)
print(fscore)