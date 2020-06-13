from random import randint, choice
from functions import get_columns, print_field
TICKET_ROWS = 3
ROWS_LEN = 18
columns_len = 9
totalField = []

totalFieldReady = False
while not totalFieldReady: 

    # Generating columns 
    totalFieldReady = True
    totalField = []
    columnsOptions = get_columns(columns_len, ROWS_LEN)
    
    for rowIndex in range(0,ROWS_LEN):
        
        # Generating rows
        rowReady = False
        row = []

        while not rowReady:
            row = []
            for index, column in enumerate(columnsOptions):
                randomChoice = choice(column)

                isTicketLastRow = rowIndex % TICKET_ROWS == TICKET_ROWS-1
                
                if(isTicketLastRow):
                    firstAbove = totalField[rowIndex-1][index]
                    secondAbove = totalField[rowIndex-2][index]
                    if (firstAbove == " " and secondAbove == " "):
                        
                        columnNumbers = [item for item in column if item.isdigit()]
                        if(len(columnNumbers)==0):
                            totalFieldReady = False
                            break
                        else: 
                            randomChoice = choice(columnNumbers)

                row.append(randomChoice)

            else:
                blanks = [item for item in row if item == " "]
                if (len(blanks) == 4):
                    rowReady = True
                continue
            break


        else: 
            for index, element in enumerate(row):
                columnsOptions[index].remove(element)

            totalField.append(row)
            continue
        break
                

print_field(totalField, TICKET_ROWS)