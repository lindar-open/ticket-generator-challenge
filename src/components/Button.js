import React from 'react';

const Button = (props) => {

  return ( 
    <div className="button" onClick={props.handleClick}>
      <p>{props.text}</p>
    </div>
  );
}
 
export default Button;