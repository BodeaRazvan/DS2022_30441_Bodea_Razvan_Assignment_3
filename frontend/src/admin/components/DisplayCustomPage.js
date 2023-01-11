import React from 'react';

export default function DisplayCustomPage({message}) {
    return (
        <div>
            <h1>{message}</h1>
        </div>
    )
}

DisplayCustomPage.defaultProps = {}