
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