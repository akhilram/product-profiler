__author__ = 'majisha'

import sys
import re


def main():

    feature_file = open(sys.argv[1],'r+')
    svm_file = open(sys.argv[2],'r+')

    output_file = open(sys.argv[3],'w+')

    features = []
    ratings = []

    i=0
    for line in feature_file:
        features.append(re.split(r'\s',line.rstrip())[0])
        i+=1

    j = 0
    for line in svm_file:
        rating = float(line.rstrip())
        if rating < 0:
            ratings.append(0.0)
        elif rating > 5:
            ratings.append(5.0)
        else:
            ratings.append(rating)
        j+=1

    for i in range(len(features)):
        output_file.write(features[i]+":"+str(ratings[i])+"\n")

    return


if __name__ == '__main__':
    main()