//this file is part of the 100stack.html page

const Bar = ({ data }) => {
    return /*#__PURE__*/ (
        React.createElement("div", { className: "BarChart" },
            data &&
            data.map(d => {
                return /*#__PURE__*/ (
                    React.createElement("div", {
                            className: "BarData",
                            style: { background: `${d.color}`, height: `${d.percent}%` }
                        }, /*#__PURE__*/

                        React.createElement("p", { className: "PercentText" }, d.percent + "%")));


            })));


};

const stack100 = () => {
    const data = [{ color: '#d41414', percent: 80 }, { color: '#14d444', percent: 20 }];
    // third coulour could be added: , { color: '#ebb860', percent: 19 }
    return /*#__PURE__*/ (
        React.createElement("div", { className: "stack100" }, /*#__PURE__*/
            React.createElement(Bar, { data: data })));


};


ReactDOM.render( /*#__PURE__*/ React.createElement(stack100, null), document.querySelector("#stack100"));