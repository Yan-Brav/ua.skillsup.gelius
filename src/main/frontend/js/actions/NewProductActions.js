import Dispatcher from "../dispatcher/Dispatcher";
import UrlConstants from "../constants/Url";
import EventConstants from "../constants/Events";
import NewProductStore from "../stores/NewProductStore";
import WorkCentersStore from "../stores/WorkCentersStore";
import ObjectConstants from '../constants/Objects';
import L from "../utils/Logging";

var NewProductActions = {

    loadAllDictionaries () {
        $.ajax({
            type: 'GET',
            contentType: "application/json",
            url: UrlConstants.LOAD_ALL_DICTIONARIES_URL,
            data: '',
            dataType: 'JSON',
            timeout: 100000,
            success: function (response) {
                if (!response["data"]) {
                    L.log("Empty 'data' in response on '" + UrlConstants.LOAD_ALL_DICTIONARIES_URL + "'");
                    return;
                }

                if (response["data"]) {
                    Dispatcher.dispatch({
                        eventType: EventConstants.LOAD_ALL_DICTIONARIES,
                        parameters: response.data
                    });
                }

            }.bind(this),
            error: function (e) {
                L.log("ERROR: ", e);
            }.bind(this)
        });
    },

    loadNewProductNumber() {
        $.ajax({
            type: 'GET',
            contentType: "application/json",
            url: UrlConstants.LOAD_PRODUCT_NUMBER_URL,
            data: '',
            dataType: 'JSON',
            timeout: 100000,
            success: function (response) {
                if (!response["data"]) {
                    L.log("Empty 'newProductNumber' in response on '" + UrlConstants.LOAD_PRODUCT_NUMBER_URL + "'");
                    return;
                }

                if (response["data"]) {
                    Dispatcher.dispatch({
                        eventType: EventConstants.LOAD_PRODUCT_NUMBER,
                        productNumber: response.data
                    });
                }

            }.bind(this),
            error: function (e) {
                L.log("ERROR: ", e);
            }.bind(this)
        });
    },

    saveProduct(){
        var newProduct = NewProductStore.getNewProduct();
        newProduct["workabilityNotes"] = WorkCentersStore.__getWorkCentersFromModal();

        L.log(newProduct);
        $.ajax({
            type: 'POST',
            url: UrlConstants.SAVE_PRODUCT_URL,
            contentType: 'application/json',
            data: JSON.stringify(newProduct),
            dataType: 'JSON',
            timeout: 100000,
            success: function (response) {
                if (response.code === 200) {
                    WorkCentersStore.setUseDefautCenters(true);
                    this.updateWorkabilityInfo();
                    $(".notes-textarea").val("");
                    L.log("product (" + response.data.savedProductNumber + ") was saved");
                    L.log("new product number (" + response.data.newProductNumber + ") was received");
                }
                Dispatcher.dispatch({
                    eventType: EventConstants.SAVE_NEW_PRODUCT,
                    response: response
                });
            }.bind(this),
            error: function (e) {
                L.log("ERROR: ", e);
            }.bind(this)

        });

    },

    updateWorkabilityInfo() {
        Dispatcher.dispatch({
            eventType: EventConstants.UPDATE_WORKABILITY_INFO
        });
    },

    updateWorkCenterNote(groupPriority, note) {
        Dispatcher.dispatch({
            eventType: EventConstants.UPDATE_WORK_CENTER_NOTE,
            groupPriority: groupPriority,
            note: note
        });
    },

    addFileLink() {
        Dispatcher.dispatch({
            eventType: EventConstants.ADD_FILE_LINK,
            someData: 42
        });
    },

    removeFileLink() {
        Dispatcher.dispatch({
            eventType: EventConstants.REMOVE_FILE_LINK,
            someData: 42
        });
    },

    /*triggerFullSave() {
     L.log("NewProductAction.triggerFullSave()");
     Dispatcher.dispatch({
     eventType: EventConstants.NEW_PRODUCT_CHANGE_TRIGGER
     });
     },*/

    saveFileLinks() {
        var savedProductNumber = NewProductStore.savedProductNumber;
        L.log("NewProductActions.saveFileLinks(): получили из хранилища номер сохраненной техкарты: " + savedProductNumber);

        //TODO Remove this block to UploadFilesStore; get this data from UploadFilesStore
        var formData = new FormData();
        $(".attachments :file").each(function () {
            var file = this.files[0];
            var name = file.name, size = file.size, type = file.type;
            //TODO size and type validation
            L.log("Найден файл: name=" + name + ", size=" + size + ", type=" + type);
            if (size > ObjectConstants.UPLOADED_FILE_SIZE_LIMIT) {
                alert("Размер одного из файлов превышает допустимые " + "0" + " (МБ)"); //TODO проверять еще перед сохранением сущности
            }
            formData.append("files", file);
        });
        formData.append("productNumber", savedProductNumber);
        //End of block

        $.ajax({
            url: UrlConstants.SAVE_PRODUCT_FILES_URL,
            type: "POST",
            dataType: "json",
            data: formData,
            /*headers: {
             "Content-Type": "charset=UTF-8"
             },*/
            processData: false, //(tell jQuery not to process the data)
            contentType: false, //(tell jQuery not to set contentType)
            //contentType maybe need for encoding ("...; encoding=UTF-8"), cuz we have bad russian filenames, that arrived on server
            success: function (data) {
                //L.log(data);
                L.log("NewProductActions.saveFileLinks(): файлы успешно сохранены");
                L.log("NewProductActions.saveFileLinks(): 1...");
                //TODO вызвать событие успешного сохранения всех данных (по факту - сущность и ее файлы-ссылки):
                /*Dispatcher.
                 dispatch({ eventType: EventConstants.SAVE_FILE_LINKS_OF_NEW_PRODUCT }).
                 then(function() {  //because file saving is last operation is saving chain
                 this.triggerFullSave()
                 })
                 ;*/
                L.log("NewProductActions.saveFileLinks(): ...2");
            },
            error: function (xhr, status) {
                alert("NewProductActions.saveFileLinks(): ошибка запроса при сохранении файлов\nstatus=" + status);
                //L.log(xhr);
                $("BODY").html(xhr.responseText);
                //TODO вызвать событие ошибки сохранения файлов (но упомянуть, что сущность сохранилась)
            }
        });

    },

    __disablePalletDictionaryDependsFromChangePacking(){
        var palletContext = $('#pallet');
        $('#packing').change(function () {
            if (this.value === '' || this.value === '1') {
                palletContext.prop("disabled", true);
                palletContext.val('');
            }
            else {
                palletContext.prop("disabled", false);
                palletContext.val('1');
            }
        });
    },

    __disablePalletDictionaryByDefault(){
        var value = $('#packing').find(':selected').val();
        if (value === '' || value === '1') {
            $('#pallet').prop("disabled", true);
        }
    },

    __defaultConnectionValveDictionaryDependsFromProductType(){
        $('#productType').change(function () {
            if (this.value === '1') {
                $('#connectionValve').val('1');
            }
            else $('#connectionValve').val('');
        });
    },

    __sumBigovki: function () {
        $(".sumBigovki").keyup(function () {
            var isEmpty = true;
            var total = 0;
            $('.sumBigovki').each(function () {
                total = total + Number($(this).val());
                if ($.trim($(this).val()) !== '') {
                    isEmpty = isEmpty && false;
                }
            });
            if (isEmpty === false) {
                $('input#sizeWorkpieceWidth').prop("disabled", true);
                $('input#sizeWorkpieceWidth').val(total);
            }
            else {
                $('input#sizeWorkpieceWidth').prop("disabled", false);
                $('input#sizeWorkpieceWidth').val("");
            }
        });
    },


    __checkInputNumber: function () {
        $(".numberInputCheck").keydown(function (event) {
            if ((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)) {
            }
            else if (event.keyCode != 8 && event.keyCode != 46) {
                event.preventDefault();
            }
        });
    }
};

export default NewProductActions;