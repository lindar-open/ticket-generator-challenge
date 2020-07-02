import React from 'react';
import PropTypes from 'prop-types'
import Field from './Field'

const TicketRow = (props) => {
    return (
      <div className="ticket-row" >
        {props.row.map((element, index) => <Field key={index} value={element} />)}
      </div>
    )
}

TicketRow.propTypes = {
    row: PropTypes.array,
}
 
export default TicketRow;