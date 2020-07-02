function getColumnOptions(min, max, blanksLen) {
  let numbers = []
  let blanks = []

  for(let i=min; i<=max; i++) {
    numbers.push(i.toString())
  }

  for(let i=0; i<blanksLen; i++) {
    blanks.push(" ")
  }

  return numbers.concat(blanks)
}

function getColumns(num, rowsLen) {
  let result = []
  for(let i=0; i<num; i++) {
    const stepStart = i*10+1
    const stepEnd = stepStart + 9
    const columnOptions = getColumnOptions(stepStart, stepEnd, rowsLen-10)
    result.push(columnOptions)
  }
  return result
}

export function generateFields(rowsNum, columnsNum, ticketRowsNum) {
  let totalFieldsReady = false
  let totalField = []
  while (!totalFieldsReady) {
    //Generating columns
    totalFieldsReady = true
    totalField = []
    let columnOptions = getColumns(columnsNum, rowsNum)
  rowsloop:  
    for (let rowIndex=0; rowIndex<rowsNum; rowIndex++) {
      //Generating rows
      let rowReady = false 
      let row = []
      while (!rowReady) {
        row = []

        for (const [index, column] of columnOptions.entries()) {

          const randomChoiceIndex = Math.floor(Math.random() * column.length)
          let randomChoice = column[randomChoiceIndex]
          const isTicketLastRow = rowIndex % ticketRowsNum === ticketRowsNum-1

          if(isTicketLastRow) {
            const firstAbove = totalField[rowIndex-1][index]
            const secondAbove = totalField[rowIndex-2][index]
            
            if(firstAbove === " " && secondAbove === " ") {
              const columnNumbers = column.filter(element => !Number(element) == 0)
              
              if(!columnNumbers.length) {
                totalFieldsReady = false
                break rowsloop
              } else {
                const randomChoiceIndex = Math.floor(Math.random() * columnNumbers.length)
                randomChoice = columnNumbers[randomChoiceIndex]
              }
            }
          } 
  
          row.push(randomChoice)
        }
  
        const blanks = row.filter(element => element === " ")
        if(blanks.length === 4) {
          rowReady = true
        }
        
      }
      
      for (const [index, element] of row.entries()) {
        const sel = columnOptions[index].indexOf(element)
        columnOptions[index].splice(sel, 1)
      }
      totalField.push(row)
    }
  }
  
  return totalField
}
