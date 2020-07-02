import React from 'react';
import PropTypes from 'prop-types'
import TicketRow from './TicketRow'

const Ticket = (props) => {
    return (
      <div className="ticket">
        {props.rows.map((row, index) => <TicketRow key={index} row={row} />)}
      </div>
    )
}

Ticket.propTypes = {
    rows: PropTypes.array,
}
 
export default Ticket;