import React, { useState } from 'react';
import PropTypes from 'prop-types'
import Ticket from './Ticket'
import Button from './Button'
import { generateFields } from '../helpers'

const Board = (props) => {
    const rows = 18
    const columns = 9
    const ticketHeight = 3 

    const [fields, setFields] = useState(generateFields(rows, columns, ticketHeight))
    const tickets = getTicketsFromFields(fields)

    function getTicketsFromFields(fields) {
        let result = []
        for(let i=0; i<fields.length; i=i+ticketHeight) {
            result.push(fields.slice(0,ticketHeight))
        }
        return result
    }

    function handleClick() {
        setFields(generateFields(rows, columns, ticketHeight))
    }
    
	return ( 
    <div className="board">
        <Button text="Reset" handleClick={handleClick}/>
        {tickets.map((ticket, index) => {
            return (
                <Ticket key={index} rows={ticket}/>
            )
        })}
    </div>
	);
}

Board.propTypes = {
    fields: PropTypes.array,
}
 
export default Board;