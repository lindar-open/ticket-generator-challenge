from random import randint, choice
ticket_rows = 3
rows = 18
columns = 9

def get_random_list(_len, _min, _max):
    result = []
    while (len(result) < _len):
        num = randint(_min,_max)
        if not num in result:
            result.append(num)
    return result

def get_column_options(_min, _max, blanks):
    numbers = [str(i) for i in range(_min,_max+1)]
    blanks =  [" " for i in range(0, blanks)]
    return numbers+blanks

def get_columns():
    result = []
    for i in range(0,columns):
        step_start = i*10+1
        step_end = step_start+9
        # print(str(step_start) + "," + str(step_end))
        result.append(get_column_options(step_start,step_end, rows-10))
    return result


# print(get_columns())
fields = []

# floor_reached = True
error = True
while (error): 
    error = False
    fields = []
    columns_free_options = get_columns()
    for row_num in range(0,rows):
        
        enable = False
        row = []
        # print(f"{row_num}, {row_num%3}")

        while not enable:
            row = []
            for index, column in enumerate(columns_free_options):
                random_choice = choice(column)

                isTicketFirstRow = row_num % ticket_rows == ticket_rows-1
                
                
                if(isTicketFirstRow):
                    firstAbove = fields[row_num-1][index]
                    secondAbove = fields[row_num-2][index]
                    if (firstAbove == " " and secondAbove == " "):
                        # print(f"case in {row_num}, {index}")
                        
                        columnNumbersOnly = [el for el in column if el.isdigit()]
                        if(len(columnNumbersOnly)==0):
                            error = True
                            print("ERROR")
                        else: 
                            random_choice = choice(columnNumbersOnly)
                        # while(random_choice == " "):
                            # print("change blank")
                    
                row.append(random_choice)
            
            num_of_blanks = len(list(filter(lambda x: x == " ", row)))
            if (num_of_blanks == 4):
                enable = True

            # if(row_num % ticket_rows == 0):


        for index, element in enumerate(row):
            columns_free_options[index].remove(element)

        fields.append(row)
                
        # print(num_of_blanks)


for index, row in enumerate(fields):
    for element in row:
        if(element == " "):
            print("  ", end=" ")
        else: 
            print(f"{int(element):02d}", end=" ")
    print()
    if(index % ticket_rows == ticket_rows-1):
        print("---"*columns)
    
    
    # if floor_reached:
    #     step_start = step_start + 1
    
    
    # floor_reached = choice([True, False])

    # if floor_reached:
    #     step_end = step_end + 1



# for  


# print (columns_free_numbers)





# a = generate_random_list(10, 0, 10)
# print(a)