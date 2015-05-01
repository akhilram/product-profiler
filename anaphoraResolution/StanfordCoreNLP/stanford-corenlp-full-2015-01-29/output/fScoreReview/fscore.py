import sys
from nltk import word_tokenize
import os

def format_lines(lines):
    new_lines=[]
    for line in lines:
        line=line.replace('/',' / ')
        line=line.replace('*',' * ')
        line=line.replace('.',' . ')
        line=line.replace('-',' - ') 
        line=line.replace('!',' ! ')
        line=line.replace('"',' " ')
        line=line.replace('[',' [ ')
        line=line.replace(']',' ] ')
        line=line.replace("'"," ' ")     
        line=line.replace(',',' , ')
        line=line.replace(':',' : ') 
        line=line.replace(';',' ; ')        
        line=line.replace('~',' ~ ')
        line=line.replace('?',' ? ')
        line=line.replace('  ',' ')
        new_lines.append(line)
    return new_lines

def format_corenlp(lines):
    new_lines=[]
    for line in lines:
        line=line.replace('.',' . ')
        line=line.replace('  ',' ')
        new_lines.append(line)
    return new_lines

class Scorer:
    def __init__(self):
        self.total_expected=0
        self.total_matched=0
        self.total_corrected=0
        
    def  load_file(self,original_file, annotated_file, generated_file):
        with open(original_file,'r') as original_file:
            self.original_lines=format_lines(original_file.readlines())

        with open(annotated_file,'r') as annotated_file:
            self.annotated_lines=format_lines(annotated_file.readlines())

        with open(generated_file,'r') as generated_file:
            self.generated_lines=format_corenlp(generated_file.readlines())
  
    def compare(self):
        sentence_count=0
        while sentence_count<len(self.annotated_lines):
            word_count=0
            ann_words = word_tokenize(self.annotated_lines[sentence_count].lower())
            gen_words=word_tokenize(self.generated_lines[sentence_count].lower())
            org_words=word_tokenize(self.original_lines[sentence_count].lower())
            while word_count<len(ann_words):
                #print
                #print(org_words[word_count])
                #print(ann_words[word_count])
                #print(gen_words[word_count])
                if(org_words[word_count]!=ann_words[word_count]): 
                    #print('Expected') 
                    self.total_expected+=1
                    if (ann_words[word_count]==gen_words[word_count]): 
                        #print('Matched')  
                        self.total_matched+=1
                if(gen_words[word_count]!=org_words[word_count] and gen_words[word_count]!='-lrb-' and gen_words[word_count]!='-rrb-' and gen_words[word_count]!='quantity' and gen_words[word_count]!="''"):
                    #print('Corrected')     
                    self.total_corrected+=1   
                #if(word_count%20==0):   ip=input("Enter: ")
                word_count+=1 
            sentence_count+=1
        #print(self.total_expected)
        #print(self.total_matched)
        #print(self.total_corrected)
            precision=float(self.total_matched)/float(self.total_corrected)
            recall=float(self.total_matched)/float(self.total_expected)
            fscore=(2*precision*recall)/(precision+recall)
            print(precision)
            print(recall)
            print(fscore)          
            #ip=input('Enter: ')


if __name__=="__main__":
    # arg 1 is the original file list
    # arg 2 is the manually annotated folder location
    # arg 3 is newly created file location
    scorer=Scorer()
    for file_ip in os.listdir(sys.argv[1]):
        scorer.load_file(sys.argv[1]+'/'+file_ip,sys.argv[2]+'/ann_'+file_ip,sys.argv[3]+'/gen_'+file_ip)
        fscore=scorer.compare()
    
