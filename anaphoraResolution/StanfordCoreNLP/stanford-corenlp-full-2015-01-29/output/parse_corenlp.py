from bs4 import BeautifulSoup

class Parser:
    def __init__(self,filename):
        with open(filename,"r") as output_file:
            self.text=output_file.read()   
        self.data=BeautifulSoup(self.text)
       
    def parse(self):
        parent=self.data.find('coreference')
        corefs=parent.find_all('coreference')
        for coref in corefs:
            print('COREF')
            mentions=coref.find_all('mention')
            for mention in mentions:
                sentence_id=mention.find('sentence').get_text()
                sentence_start=mention.find('start').get_text()
                sentence_end=mention.find('end').get_text()
                text=mention.find('text').get_text()
                print(text+" : "+sentence_id+" : "+sentence_start+" : "+sentence_end)
            ip=input("Enter")
    
    def read_text(self):
        self.sentences=[]
        parent=self.data.find('sentences')
        sentences=parent.find_all('sentence')
        for sentence in sentences:
            words=[]
            tokens=sentence.find_all('token')
            for token in tokens:
                text=token.find('word').get_text()
                pos=token.find('pos').get_text()
                words.append((text,pos))
            self.sentences.append(words)
        print(self.sentences)
 

if __name__=="__main__":
    parser=Parser("test.txt.xml")
    #sentences=parser.read_text()
    sentences=parser.parse()
    
    
