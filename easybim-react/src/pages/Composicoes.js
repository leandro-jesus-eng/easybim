import React, { useState, useEffect, useRef } from 'react';
import classNames from 'classnames';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dropdown } from 'primereact/dropdown';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { FileUpload } from 'primereact/fileupload';
import { Rating } from 'primereact/rating';
import { Toolbar } from 'primereact/toolbar';
import { InputTextarea } from 'primereact/inputtextarea';
import { RadioButton } from 'primereact/radiobutton';
import { InputNumber } from 'primereact/inputnumber';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';

import { MenuOptions } from '../components/MenuOptions';
import { ComposicoesService } from '../service/ComposicoesService';

const Composicoes = () => {
    let emptyProduct = {
        id: null,
        name: '',
        image: null,
        description: '',
        category: null,
        price: 0,
        quantity: 0,
        rating: 0,
        inventoryStatus: 'INSTOCK'
    };

    const [composicoes, setComposicoes] = useState(null);
    const [productDialog, setProductDialog] = useState(false);
    const [deleteProductDialog, setDeleteProductDialog] = useState(false);
    const [deleteProductsDialog, setDeleteProductsDialog] = useState(false);
    const [product, setProduct] = useState(emptyProduct);
    const [selectedRows, setSelectedRows] = useState(null);
    const [submitted, setSubmitted] = useState(false);
    const [globalFilter, setGlobalFilter] = useState(null);
    const toast = useRef(null);
    const dt = useRef(null);
    
    const [selectedNomeTabela, setSelectedNomeTabela] = useState(null);
    const [nomeTabelas, setNomeTabelas] = useState(null);
    const onNomeTabelaChange = (e) => {
        setSelectedNomeTabela(e.value);
    }

    const [selectedLocalidade, setSelectedLocalidade] = useState(null);
    const [localidades, setLocalidades] = useState(null);
    const onLocalidadeChange = (e) => {
        setSelectedLocalidade(e.value);
    }

    useEffect(() => {
        const composicoesService = new ComposicoesService();
        composicoesService.getNomeTabelas(setNomeTabelas);
        composicoesService.getComposicoes().then(data => setComposicoes(data));        
    }, []);

    const formatCurrency = (value) => {
        return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    }

    const openNew = () => {
        setProduct(emptyProduct);
        setSubmitted(false);
        setProductDialog(true);
    }

    const hideDialog = () => {
        setSubmitted(false);
        setProductDialog(false);
    }

    const hideDeleteProductDialog = () => {
        setDeleteProductDialog(false);
    }

    const hideDeleteProductsDialog = () => {
        setDeleteProductsDialog(false);
    }

    const saveProduct = () => {
        /*setSubmitted(true);

        if (product.name.trim()) {
            let _products = [...composicoes];
            let _product = { ...product };
            if (product.id) {
                const index = findIndexById(product.id);

                _products[index] = _product;
                toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Product Updated', life: 3000 });
            }
            else {
                _product.id = createId();
                _product.image = 'product-placeholder.svg';
                _products.push(_product);
                toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Product Created', life: 3000 });
            }

            setComposicoes(_products);
            setProductDialog(false);
            setProduct(emptyProduct);
        }*/
    }

    const editProduct = (composicao) => {
        /*setProduct({ ...product });
        setProductDialog(true);*/
    }

    const confirmDeleteProduct = (composicao) => {
        /*setProduct(product);
        setDeleteProductDialog(true);*/
    }

    const deleteProduct = () => {
        /*let _products = products.filter(val => val.id !== product.id);
        setProducts(_products);
        setDeleteProductDialog(false);
        setProduct(emptyProduct);
        toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Product Deleted', life: 3000 });*/
    }

    const findIndexById = (id) => {
        let index = -1;
        for (let i = 0; i < composicoes.length; i++) {
            if (composicoes[i].codigoComposicao === id) {
                index = i;
                break;
            }
        }

        return index;
    }

    const createId = () => {
        let id = '';
        let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        for (let i = 0; i < 5; i++) {
            id += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return id;
    }

    const exportCSV = () => {
        dt.current.exportCSV();
    }

    const confirmDeleteSelected = () => {
        setDeleteProductsDialog(true);
    }

    const deleteSelectedProducts = () => {
        /*let _products = products.filter(val => !selectedProducts.includes(val));
        setProducts(_products);
        setDeleteProductsDialog(false);
        setSelectedProducts(null);
        toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Products Deleted', life: 3000 });*/
    }

    const onCategoryChange = (e) => {
        let _product = { ...product };
        _product['category'] = e.value;
        setProduct(_product);
    }

    const onInputChange = (e, name) => {
        const val = (e.target && e.target.value) || '';
        let _product = { ...product };
        _product[`${name}`] = val;

        setProduct(_product);
    }

    const onInputNumberChange = (e, name) => {
        const val = e.value || 0;
        let _product = { ...product };
        _product[`${name}`] = val;

        setProduct(_product);
    }

    const leftToolbarTemplate = () => {
        return (
            <React.Fragment>
                <div className="my-2">
                    <Button label="New" icon="pi pi-plus" className="p-button-success mr-2" onClick={openNew} disabled={true} />
                    <Button label="Delete" icon="pi pi-trash" className="p-button-danger" onClick={confirmDeleteSelected} disabled={!selectedRows || !selectedRows.length} />
                </div>
            </React.Fragment>
        )
    }

    const rightToolbarTemplate = () => {
        return (
            <React.Fragment>   
                <FileUpload mode="basic" accept=".xls" maxFileSize={1000000} label="Import" chooseLabel="Import" className="mr-2 inline-block" />
                <Button label="Export" icon="pi pi-upload" className="p-button-help" onClick={exportCSV} />
            </React.Fragment>
        )
    }

    const tabelaBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Tabela</span>
                SINAPI
            </>
        );
    }

    const localidadeBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Tabela</span>
                Campo Grande
            </>
        );
    }

    const dataPrecoBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Tabela</span>
                dezembro/2021
            </>
        );
    }

    const codeBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Code</span>
                {rowData.codigoComposicao}
            </>
        );
    }

    const descricaoBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Name</span>
                {rowData.descricaoComposicao}
            </>
        );
    }

    const medidaBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Name</span>
                {rowData.unidade}
            </>
        );
    }

    const custoMaoObraBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoMaoObra)}
            </>
        );
    }

    const custoMaterialBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoMaterial)}
            </>
        );
    }    

    const custoEquipamentoBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoEquipamento)}
            </>
        );
    }
    
    const custoTotalBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoTotal)}
            </>
        );
    } 

    

    const nameBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Name</span>
                {rowData.name}
            </>
        );
    }

    const imageBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Image</span>
                <img src={`assets/demo/images/product/${rowData.image}`} alt={rowData.image} className="shadow-2" width="100" />
            </>
        )
    }

    const priceBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.price)}
            </>
        );
    }

    const categoryBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Category</span>
                {rowData.category}
            </>
        );
    }

    const ratingBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Reviews</span>
                <Rating value={rowData.rating} readonly cancel={false} />
            </>
        );
    }

    const statusBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Status</span>
                <span className={`product-badge status-${rowData.inventoryStatus.toLowerCase()}`}>{rowData.inventoryStatus}</span>
            </>
        )
    }

    const actionBodyTemplate = (rowData) => {
        return (
            <>
                <MenuOptions></MenuOptions>
            </>
        );
    }

    const header = (
        <div className="flex flex-column md:flex-row md:justify-content-between md:align-items-center">            
            <Dropdown value={selectedNomeTabela} options={nomeTabelas} onChange={onNomeTabelaChange} optionLabel="name" placeholder="Selecione uma tabela" />
            <Dropdown value={selectedLocalidade} options={localidades} onChange={onLocalidadeChange} optionLabel="name" placeholder="Selecione uma tabela" />

            <span className="block mt-2 md:mt-0 p-input-icon-left">                
                <i className="pi pi-search" />
                <InputText type="search" onInput={(e) => setGlobalFilter(e.target.value)} placeholder="Search..." />                
            </span>
        </div>
    );

    const clearFilter = () => {
         //setGlobalFilter("olá"); 
    }
    
    const productDialogFooter = (
        <>
            <Button label="Cancel" icon="pi pi-times" className="p-button-text" onClick={hideDialog} />
            <Button label="Save" icon="pi pi-check" className="p-button-text" onClick={saveProduct} />
        </>
    );
    const deleteProductDialogFooter = (
        <>
            <Button label="No" icon="pi pi-times" className="p-button-text" onClick={hideDeleteProductDialog} />
            <Button label="Yes" icon="pi pi-check" className="p-button-text" onClick={deleteProduct} />
        </>
    );
    const deleteProductsDialogFooter = (
        <>
            <Button label="No" icon="pi pi-times" className="p-button-text" onClick={hideDeleteProductsDialog} />
            <Button label="Yes" icon="pi pi-check" className="p-button-text" onClick={deleteSelectedProducts} />
        </>
    );

    return (
        <div className="grid crud-demo">
            <div className="col-12">
                <div className="card">
                    <h5>Gerenciar Composições</h5>
                    <Toast ref={toast} />                    
                    <Toolbar className="mb-4" left={leftToolbarTemplate} right={rightToolbarTemplate}></Toolbar>

                    <DataTable ref={dt}  size="small" responsiveLayout="scroll" className="datatable-responsive"
                        value={composicoes} selection={selectedRows} onSelectionChange={(e) => setSelectedRows(e.value)}
                        dataKey="codigoComposicao" 
                        paginator rows={10} rowsPerPageOptions={[10, 15, 25]} 
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
                        globalFilter={globalFilter} globalFilterFields={['tabela', 'localidade', 'descricaoComposicao']} emptyMessage="No registers found." header={header} >                                                       
                        <Column rowReorder></Column>
                        <Column body={actionBodyTemplate}></Column>
                        <Column selectionMode="multiple" headerStyle={{ width: '3rem'}} hidden='true'></Column>                        
                        <Column field="tabela" header="Tabela" sortable body={tabelaBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                        <Column field="localidade" header="Local" sortable body={localidadeBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                        <Column field="dataPreco" header="Base" sortable body={dataPrecoBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>                        
                        <Column field="codigoComposicao" header="id" sortable body={codeBodyTemplate} headerStyle={{ width: '5%', minWidth: '2rem' }}></Column>
                        <Column field="descricaoComposicao" header="Descrição" sortable body={descricaoBodyTemplate} headerStyle={{ width: '90%', minWidth: '20rem' }}></Column>
                        <Column field="unidade" header="Med" sortable body={medidaBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>                                                
                        <Column field="custoMaoObra" header="Mão Obra" sortable body={custoMaoObraBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                        <Column field="custoMaterial" header="Material" sortable body={custoMaterialBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                        <Column field="custoEquipamento" header="Equipa mento" sortable body={custoEquipamentoBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>    
                        <Column field="custoTotal" header="Total" body={custoTotalBodyTemplate} sortable headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                    </DataTable>

                    <Dialog visible={productDialog} style={{ width: '450px' }} header="Product Details" modal className="p-fluid" footer={productDialogFooter} onHide={hideDialog}>
                        {product.image && <img src={`assets/demo/images/product/${product.image}`} alt={product.image} width="150" className="mt-0 mx-auto mb-5 block shadow-2" />}
                        <div className="field">
                            <label htmlFor="name">Name</label>
                            <InputText id="name" value={product.name} onChange={(e) => onInputChange(e, 'name')} required autoFocus className={classNames({ 'p-invalid': submitted && !product.name })} />
                            {submitted && !product.name && <small className="p-invalid">Name is required.</small>}
                        </div>
                        <div className="field">
                            <label htmlFor="description">Description</label>
                            <InputTextarea id="description" value={product.description} onChange={(e) => onInputChange(e, 'description')} required rows={3} cols={20} />
                        </div>

                        <div className="field">
                            <label className="mb-3">Category</label>
                            <div className="formgrid grid">
                                <div className="field-radiobutton col-6">
                                    <RadioButton inputId="category1" name="category" value="Accessories" onChange={onCategoryChange} checked={product.category === 'Accessories'} />
                                    <label htmlFor="category1">Accessories</label>
                                </div>
                                <div className="field-radiobutton col-6">
                                    <RadioButton inputId="category2" name="category" value="Clothing" onChange={onCategoryChange} checked={product.category === 'Clothing'} />
                                    <label htmlFor="category2">Clothing</label>
                                </div>
                                <div className="field-radiobutton col-6">
                                    <RadioButton inputId="category3" name="category" value="Electronics" onChange={onCategoryChange} checked={product.category === 'Electronics'} />
                                    <label htmlFor="category3">Electronics</label>
                                </div>
                                <div className="field-radiobutton col-6">
                                    <RadioButton inputId="category4" name="category" value="Fitness" onChange={onCategoryChange} checked={product.category === 'Fitness'} />
                                    <label htmlFor="category4">Fitness</label>
                                </div>
                            </div>
                        </div>

                        <div className="formgrid grid">
                            <div className="field col">
                                <label htmlFor="price">Price</label>
                                <InputNumber id="price" value={product.price} onValueChange={(e) => onInputNumberChange(e, 'price')} mode="currency" currency="USD" locale="en-US" />
                            </div>
                            <div className="field col">
                                <label htmlFor="quantity">Quantity</label>
                                <InputNumber id="quantity" value={product.quantity} onValueChange={(e) => onInputNumberChange(e, 'quantity')} integeronly />
                            </div>
                        </div>
                    </Dialog>

                    <Dialog visible={deleteProductDialog} style={{ width: '450px' }} header="Confirm" modal footer={deleteProductDialogFooter} onHide={hideDeleteProductDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                            {product && <span>Are you sure you want to delete <b>{product.name}</b>?</span>}
                        </div>
                    </Dialog>

                    <Dialog visible={deleteProductsDialog} style={{ width: '450px' }} header="Confirm" modal footer={deleteProductsDialogFooter} onHide={hideDeleteProductsDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                            {product && <span>Are you sure you want to delete the selected products?</span>}
                        </div>
                    </Dialog>
                </div>
            </div>
        </div>
    );
}

const comparisonFn = function (prevProps, nextProps) {
    return prevProps.location.pathname === nextProps.location.pathname;
};

export default React.memo(Composicoes, comparisonFn);