#this is to convert a file into svm format
#usage python3 svm_formatter.py --train/test input_file dictionary_file output_file

import re
import codecs
import sys
import math

def main():
    
    if len(sys.argv) < 5:
        print("Usage:  python3 svm_formatter.py --train/dev/test input_file dictionary_file output_file")
        return


    
    #open training file
    training_file = codecs.open(sys.argv[2], 'r', 'utf-8',errors='ignore')
    #open output file
    output_file = codecs.open(sys.argv[4], 'w', 'utf-8', errors = 'ignore')
  
    dict = {}
    if sys.argv[1] == '--train':
        for line in training_file:
            words = re.split(r'\s+' , line.rstrip())
            for word in words[1:]:
                if word not in dict:
                    dict[word] = 1
                
        training_file.close()    
        
        dict_file = codecs.open(sys.argv[3], 'w' , 'utf-8',errors='ignore')
        for key in sorted(dict):
            dict_file.write(str(key) + '\n')
            
        dict_file.close()
    
        
    dict_file = codecs.open(sys.argv[3], 'r' , 'utf-8',errors='ignore')
    dict = {}
    i=1
    for line in dict_file:
        word = re.split(r'\s+',line.rstrip())[0]
        
        dict[word] = i
        i+=1
    
    dict_file.close()
    
    training_file = codecs.open(sys.argv[2], 'r', 'utf-8',errors='ignore')
    
    for line in training_file:
        svm_line = ""
        word_count = {}
        feature_vector = {}
        words = re.split(r'\s+', line.rstrip())
        if sys.argv[1] == '--train':
            svm_line = words[0]+' '
                
            for word in words[1:]:
                if word in word_count:
                    word_count[word] += 1
                else:
                    word_count[word] = 1
                    
            total_words = len(words)-1
        else:
            svm_line = '0.0 '
            if sys.argv[1] == '--dev':
                n = 1
            elif sys.argv[1] == '--test':
                n = 1
            for word in words[n:]:
                if word in word_count:
                    word_count[word] += 1
                else:
                    word_count[word] = 1
            
            total_words = len(words)
            
        
        for word in word_count:
            if word in dict:
                feature_vector[word] = (dict[word] , float(word_count[word]/total_words))
            else: # will happen in case of test file
                dict[word] = len(dict)+1
                feature_vector[word] = (dict[word], float(word_count[word]/total_words))
            
        
        for item in sorted(feature_vector,key= lambda x : feature_vector[x][0]):
            tuple = feature_vector[item]
            svm_line += str(tuple[0])+':'+str(tuple[1])+' '
            
        output_file.write(svm_line.rstrip()+'\n')
        
        
    training_file.close()
    output_file.close()
    

#boilerplate for main
if __name__ == '__main__':
    main()