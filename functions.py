from random import randint, choice

def get_column_options(_min, _max, blanks_len):
    numbers = [str(i) for i in range(_min,_max+1)]
    blanks =  [" " for i in range(0, blanks_len)]
    return numbers+blanks

def get_columns(_num, rows_len):
    result = []
    for i in range(0,_num):
        step_start = i*10+1
        step_end = step_start+9
        result.append(get_column_options(step_start,step_end, rows_len-10))
    return result


def generate_field(rows_num, columns_num, ticket_rows_num):
    totalFieldReady = False

    while not totalFieldReady: 
        # Generating columns 
        totalFieldReady = True
        totalField = []
        columnsOptions = get_columns(columns_num, rows_num)
        
        for rowIndex in range(0,rows_num):
            # Generating rows
            rowReady = False
            row = []

            while not rowReady:
                row = []
                for index, column in enumerate(columnsOptions):
                    randomChoice = choice(column)
                    isTicketLastRow = rowIndex % ticket_rows_num == ticket_rows_num-1

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

    return totalField


def print_field(field, ticket_rows):
    _columns_len = len(field[0]) 
    print()
    for index, row in enumerate(field):
        for element in row:
            if(element == " "):
                print("  ", end=" ")
            else: 
                print(f"{int(element):02d}", end=" ")
        print()
        if(index % ticket_rows == ticket_rows-1):
            if (index == len(field)-1):
                print()
            else:
                print("---"*_columns_len)