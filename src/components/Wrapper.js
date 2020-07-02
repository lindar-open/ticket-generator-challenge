import React from 'react';

const Wrapper = (props) => {
	return ( 
		<main className="wrapper">
			{ props.children }
		</main> 
	);
}
 
export default Wrapper;