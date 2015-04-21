import sys
import subprocess
from parse_bart_output import Parser
import os

if __name__=="__main__":
    for filename in os.listdir('Input'):
        os.remove('Input/'+filename)
    for filename in os.listdir('Output'):
        os.remove('Output/'+filename)
    sentences=[]
    filename=sys.argv[1]
    output_folder=sys.argv[2]
    result=subprocess.call(['python','bart.py',filename,output_folder])
    for filename in sorted(os.listdir("Output")):
        parser=Parser("Output/"+filename)
        sentences.append(parser.parse())
    with open('review_output.txt','w') as output_file:
        for sentence in sentences:
            output_file.write(sentence)
            output_file.write('\n')
