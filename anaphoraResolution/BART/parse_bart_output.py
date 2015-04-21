import os
from bs4 import BeautifulSoup

class Parser:

    def __init__(self, filename):
        with open(filename,"r") as output_file:
            self.text=output_file.read()    

    def find_corefs(self,data):
         for tag in data.find_all('coref'):
            coref_id= tag['set-id']
            if coref_id not in self.coref: self.coref[coref_id]=[]
            coref_words=[]
            for word in tag.find_all('w'):
                pos=word['pos']
                text=word.getText()
                coref_words.append((text,pos))
            self.coref[coref_id].append(coref_words)

    def replace_coref(self, parent):
        coref_id=parent['set-id']  
        coref_sentences=self.coref[coref_id]
        noun_phrase=[]
        noun_phrase_found=False
        for sentence in coref_sentences:
            for word in sentence:
                if word[1]=='nn': 
                    words= [sen_tuple[0] for sen_tuple in sentence]
                    noun_phrase_found=True
                    break
        if noun_phrase_found==False or len(parent.find_all('w',{"pos":"nn"})) != 0: 
            words=parent.find_all('w')
            words=[word.get_text() for word in words]
        return words
            
                

    def parse(self):
        self.coref={}
        data=BeautifulSoup(self.text)
        # 1. get coreferences and save it in a dictionary
        self.find_corefs(data)
        # 2. create a new list of words
        # append word
        # if word is coref without nn replace it with a noun phrase
        all_words=data.find_all('w')
        final_sentence=[]
        word_count=0
        while word_count < len(all_words):
            if 'set-id' in all_words[word_count].parent.attrs: 
                new_words=self.replace_coref(all_words[word_count].parent)    
                for new_word in new_words:
                    final_sentence.append(new_word) 
                while ('set-id' in all_words[word_count+1].parent.attrs) and (word_count+1 < len(all_words))  : word_count+=1   
            else :  final_sentence.append(all_words[word_count].getText())      
            word_count+=1
        final_sentence=" ".join(final_sentence)
        print(final_sentence)
        ip=input("Enter:")
        return final_sentence


if __name__=="__main__":
    for filename in os.getdir("Output"):
        parser=Parser(filename)
        parser.parse()
