import sys
from bs4 import BeautifulSoup

class Parser:
    def __init__(self,filename):
        with open(filename,"r") as output_file:
            self.text=output_file.read()   
        self.data=BeautifulSoup(self.text)
     
    def nn_check(self,coref_details):
        for coref in coref_details:
            sentence_id=coref[1]
            sentence_start=coref[2]
            sentence_end=coref[3]
            count=int(sentence_start)-1
            while count < int(sentence_end)-1:
                word_details=self.sentences[int(sentence_id)-1][count]
                if str(word_details[1])=='NN' or str(word_details[1]) =='NNP':  
                    return word_details[0]
                count+=1
        return False

    def parse(self):
        parent=self.data.find('coreference')
        if parent is not None:
            corefs=parent.find_all('coreference')
            for coref in corefs:
                #print('COREF')
                coref_details=[]
                mentions=coref.find_all('mention')
                for mention in mentions:
                    sentence_id=mention.find('sentence').get_text()
                    sentence_start=mention.find('start').get_text()
                    sentence_end=mention.find('end').get_text()
                    text=mention.find('text').get_text()
                    coref_details.append((text,sentence_id,sentence_start,sentence_end))
                noun_phrase=self.nn_check(coref_details)
                if noun_phrase is False: continue
                self.replace_pronouns(coref_details,noun_phrase)
            #print(self.sentences_words)
        final_sentence=""
        for sentence in self.sentences_words:
            for word in sentence:
                final_sentence+=word+" "  
        return final_sentence

    def replace_pronouns(self,coref_details,noun_phrase):
        #print(noun_phrase)                
        print('Replacing Pronouns')
        for coref in coref_details:
            sentence_id=int(coref[1])
            sentence_start=int(coref[2])
            sentence_end=int(coref[3])
            count=sentence_start-1
            replacement_required=True
            while count < sentence_end-1:
                word_details=self.sentences[sentence_id-1][count]
                if str(word_details[1])=='NN' or str(word_details[1]) =='NNP':
                    replacement_required=False 
                    break           
                count+=1                  
            if replacement_required==True:
                self.sentences_words[sentence_id-1][sentence_start-1]=noun_phrase
                count=sentence_start
                while count < sentence_end-1:
                    self.sentences_words[sentence_id-1][count]=" "
                    count+=1



    def read_text(self):
        self.sentences=[]
        self.sentences_words=[]
        parent=self.data.find('sentences')
        sentences=parent.find_all('sentence')
        for sentence in sentences:
            details=[]
            words=[]
            tokens=sentence.find_all('token')
            for token in tokens:
                text=token.find('word').get_text()
                pos=token.find('pos').get_text()
                details.append((text,pos))
                words.append(text)
            self.sentences.append(details)
            self.sentences_words.append(words)
        #print(self.sentences)
        #print(self.sentences_words)
 

if __name__=="__main__":
    parser=Parser(sys.argv[1])
    parser.read_text()
    final_sentence=parser.parse()
    print(final_sentence) 
    ip=input("Enter data: ") 
    with open('/home/indu/Desktop/544/project/gitCode/product-profiler/anaphoraResolution/StanfordCoreNLP/stanford-corenlp-full-2015-01-29/output/reviews_output.txt','a') as output_file:
        output_file.write(final_sentence)
        output_file.write('\n')
    
    
