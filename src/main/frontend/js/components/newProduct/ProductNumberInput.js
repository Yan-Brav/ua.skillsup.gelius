import React from 'react';
import EventConstants from '../../constants/Events';
import NewProductStore from '../../stores/NewProductStore';

class ProductNumberInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isNewProduct: true,
            newProductNumber: ""
        };
        this.__changeCheckbox = this.__changeCheckbox.bind(this);
        this.__changeText = this.__changeText.bind(this);
        this.__loadDefaultValues = this.__loadDefaultValues.bind(this);
    }

    componentWillMount() {
        NewProductStore.addListener(EventConstants.NEW_PRODUCT_CHANGE_EVENT, this.__loadDefaultValues);
    }

    componentWillUnmount() {
        NewProductStore.removeListener(EventConstants.NEW_PRODUCT_CHANGE_EVENT, this.__loadDefaultValues);
    }

    render() {
        return (
            <div className="header_title_input">
                <input
                    type="text" className="form-control numberInputCheck" id="productNumber" contenteditable="false"
                    value={this.state.newProductNumber} onChange={this.__changeText}
                    disabled={this.state.isNewProduct}
                />
                <div className="isNew">
                    <input type="checkbox" id="isNew" checked={this.state.isNewProduct}
                           onChange={this.__changeCheckbox}/>
                    <p>Новая карта</p>
                </div>
            </div>
        );
    }

    __changeCheckbox(e) {
        var isNewProduct = e.target.checked;
        this.setState({isNewProduct: isNewProduct});

        if (isNewProduct)
            this.setState({newProductNumber: NewProductStore.getDefaultProductProperty("productNumber")});
        else
            this.setState({newProductNumber: ""});
    }

    __changeText(e) {
        this.setState({newProductNumber: e.target.value});
    }

    __loadDefaultValues() {
        if (NewProductStore.isEnableDefaultValues()) {
            this.setState({
                newProductNumber: NewProductStore.getDefaultProductProperty("productNumber"),
                isNewProduct: NewProductStore.getDefaultProductProperty("isNew")
            });
        }
    }
}

export default ProductNumberInput;