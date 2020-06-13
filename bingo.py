from functions import generate_field, print_field

ROWS_NUM = 18
COLUMNS_NUM = 9
TICKET_ROWS = 3

totalField = []
                
totalField = generate_field(ROWS_NUM,COLUMNS_NUM,TICKET_ROWS)

print_field(totalField, TICKET_ROWS)