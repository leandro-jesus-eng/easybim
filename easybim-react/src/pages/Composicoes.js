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
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { OrganizationChart } from 'primereact/organizationchart';

import { MenuOptions } from '../components/MenuOptions';
import { ComposicoesService } from '../service/ComposicoesService';
import './ComposicaoChart.css'

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

    const [loading, setLoading] = useState(false);
    const [composicoes, setComposicoes] = useState(null);
    const [composicaoDialog, setComposicaoDialog] = useState(false);
    const [composicaoSelected, setComposicaoSelected] = useState(null);
    const [deleteComposicaoDialog, setDeleteComposicaoDialog] = useState(false);
    const [deleteProductsDialog, setDeleteProductsDialog] = useState(false);
    const [product, setProduct] = useState(emptyProduct);
    const [selectedRows, setSelectedRows] = useState(null);
    const [submitted, setSubmitted] = useState(false);
    const [globalFilter, setGlobalFilter] = useState(null);
    const toast = useRef(null);
    const dt = useRef(null);
    const composicoesService = new ComposicoesService();
    
    const getComposicoes = (nomeTabela, localidade, dataPreco) => {        
        console.log("t="+nomeTabela + " l="+ localidade+" d="+dataPreco);
        if(nomeTabela && localidade && dataPreco) {
            setLoading(true);
            composicoesService.getComposicoes(nomeTabela, localidade, dataPreco).then(data => { setComposicoes(data); setLoading(false); });            
        }
    }

    const [selectedNomeTabela, setSelectedNomeTabela] = useState(null);
    const [nomeTabelas, setNomeTabelas] = useState(null);
    const onNomeTabelaChange = (e) => {   
        setSelectedNomeTabela(e.value);

        composicoesService.getLocalidades(e.value.name).then(data => setLocalidades(data));        
        composicoesService.getDataPrecos(e.value.name).then(data => setDataPrecos(data));                

        getComposicoes(e.value.name, selectedLocalidade, selectedDataPreco );
    }

    const [selectedLocalidade, setSelectedLocalidade] = useState(null);
    const [localidades, setLocalidades] = useState(null);
    const onLocalidadeChange = (e) => {
        setSelectedLocalidade(e.value);        
        
        getComposicoes(selectedNomeTabela.name, e.value, selectedDataPreco );
    }

    const [selectedDataPreco, setSelectedDataPreco] = useState(null);
    const [dataPrecos, setDataPrecos] = useState(null);
    const onDataPrecoChange = (e) => {
        setSelectedDataPreco(e.value);        
        
        getComposicoes(selectedNomeTabela.name, selectedLocalidade, e.value );
    }
    

    useEffect(() => {                
        composicoesService.getNomeTabelas().then(data => setNomeTabelas(data));          
    }, []);

    const formatCurrency = (value) => {
        return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    }

    const openNew = () => {
        setProduct(emptyProduct);
        setSubmitted(false);
        setComposicaoDialog(true);
    }

    const hideDialog = () => {
        setSubmitted(false);
        setComposicaoDialog(false);
    }

    const hideDeleteComposicaoDialog = () => {
        setDeleteComposicaoDialog(false);
    }

    const hideDeleteProductsDialog = () => {
        setDeleteProductsDialog(false);
    }

    const deleteComposicao = () => {
        /*let _products = products.filter(val => val.id !== product.id);
        setProducts(_products);
        setDeleteComposicaoDialog(false);
        setProduct(emptyProduct);*/
        toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Composicao Deleted', life: 3000 });
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
                    <Button label={"Delete "+( (selectedRows && selectedRows.length>0)? "("+selectedRows.length+")":"" )} icon="pi pi-trash" className="p-button-danger" onClick={confirmDeleteSelected} disabled={!selectedRows || !selectedRows.length} />
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
    
    const dataTest = [{
        label: 'F.C Barcelona',
        expanded: true,
        type: 'COMPOSICAO',
        className: 'p-composicao',
        children: [
            {
                label: 'F.C Barcelona',
                expanded: true,
                type: 'COMPOSICAO',
                className: 'p-composicao',
                children: [
                    {
                        label: 'Chelsea FC',
                        type: 'INSUMO',
                        className: 'p-insumo',
                    },
                    {
                        label: 'F.C. Barcelona',
                        type: 'INSUMO',
                        className: 'p-insumo',
                    }
                ]
            },
            {
                label: 'Real Madrid',
                expanded: true,
                children: [
                    {
                        label: 'Bayern Munich',
                        type: 'INSUMO',
                        className: 'p-insumo',
                    },
                    {
                        label: 'Real Madrid',
                        type: 'INSUMO',
                        className: 'p-insumo',
                    }
                ]
            }
        ]
    }];

    const [composicaoDataChart, setComposicaoDataChart] = useState([]);
    let dataAux = [];
    const getComposicaoChart = (composicao) => {        
        dataAux = [];
        getComposicaoChartRecursive(composicao, dataAux);
        setComposicaoDataChart(dataAux);        
    }
    const getComposicaoChartRecursive = (composicao, data) =>{
        if(!composicao)
            return;

        data.push ({
            children: [{
                children : [],
                label: composicao.codigoComposicao,
                type: 'COMPOSICAO',
                className: 'p-composicao',
                expanded: true,                
                data: { 'composicao': composicao },
            }],
            label: composicao.codigoComposicao,
            type: 'COMPOSICAO',
            className: 'p-composicao',
            expanded: true,
            data: { 'composicao': composicao },           
        });
        let lastIndex = data.length - 1;

        composicao.itensComposicao.forEach( (itemComposicao) => {                
                if(itemComposicao.tipoItem == 'COMPOSICAO') {
                    // TODO buscar a composicao no servidor                    
                    composicoesService.getComposicaoById(itemComposicao.id).then( comp => getComposicaoChartRecursive(comp , data[lastIndex].children) );                                                        
                } else {
                    data[lastIndex].children.push( {
                        label: itemComposicao.codigoItem,
                        type: 'INSUMO',
                        className: 'p-insumo',
                        expanded: true,
                        data: { 'insumo': itemComposicao },        
                    });
                }
            }
        );        
        setComposicaoDataChart(dataAux);
    }

    const nodeTemplate = (node) => {
        /*if (node.type === "COMPOSICAO") {
            return (
                <div style={{ width: '200px' }}>
                    <div className="node-header">{node.label}</div>
                    <div className="node-content">
                        <div>Descrição: {node.data.composicao.descricaoComposicao}</div>
                        <div>Custo Mão de Obra: {node.data.composicao.custoMaoObra}</div>                            
                        <div>Custo Material: {node.data.composicao.custoMaterial}</div>                            
                        <div>Custo Equipamento: {node.data.composicao.custoEquipamento}</div>                            
                        <div>Custo Serviços Terceiros: {node.data.composicao.custoServicosTerceiros}</div>         
                        <div>Outros Custos: {node.data.composicao.custosOutros}</div>       
                        <div>Custo Total: {node.data.composicao.custoTotal}</div>                            
                    </div>
                </div>
            );
        } else { // INSUMO
            return (
                <div style={{ width: '200px' }}>
                    <div className="node-header">{node.label}</div>
                    <div className="node-content">
                        <div>Descrição: {node.data.insumo.descricaoItem}</div>
                        <div>Preço Unitário: {node.data.insumo.precoUnitarioItem}  {node.data.insumo.precoUnitarioItem}</div>
                        <div>Coeficiente: {node.data.insumo.coeficienteItem}</div>
                        <div>Custo Total: {node.data.insumo.custoTotalItem}</div>
                    </div>
                </div>
            );
        }*/
        return (
            <div style={{ width: '200px' }}>
                <div className="node-header">{node.label}</div>
                <div className="node-content">
                    <div>{ (node.type)? node.type: "teste" }</div>                         
                </div>
            </div>
        );
    }

    

    const actionBodyTemplate = (composicao) => {

        const itemsMenuComposicao = [
            { 
                label:'Árvore de fatores',
                icon:'pi pi-fw pi-sitemap',
                command: () => { 
                    setComposicaoSelected(composicao); 
                    getComposicaoChart(composicao);
                    setComposicaoDialog(true); 
                }
                
            },
            {
                separator:true
            },
            {
                label:'Criar composição própria',
                icon:'pi pi-fw pi-sign-in',                
                command: () => {alert(composicao.codigoComposicao)}
            },
            {
                label:'Remover composição',
                icon:'pi pi-fw pi-trash',
                command: () => {alert(composicao.codigoComposicao)}
            }
        ];

        return (
            <>
                <MenuOptions items={itemsMenuComposicao}></MenuOptions>
            </>
        );
    }

    const header = (
        <div className="flex flex-column md:flex-row md:justify-content-between md:align-items-center">    
            <div>
                <Dropdown value={selectedNomeTabela} options={nomeTabelas} onChange={onNomeTabelaChange} optionLabel="name" placeholder="Selecione uma tabela" />
                <Dropdown value={selectedLocalidade} options={localidades} onChange={onLocalidadeChange} placeholder="Selecione uma localidade" />
                <Dropdown value={selectedDataPreco} options={dataPrecos} onChange={onDataPrecoChange} onplaceholder="Selecione data base" />
            </div>        

            <span className="block mt-2 md:mt-0 p-input-icon-left">                
                <i className="pi pi-search" />
                <InputText type="search" onInput={(e) => setGlobalFilter(e.target.value)} placeholder="Search..." />                
            </span>
        </div>
    );

    const clearFilter = () => {
         //setGlobalFilter("olá"); 
    }
    
    const composicaoDialogFooter = (
        <>
            <Button label="Cancel" icon="pi pi-times" className="p-button-text" onClick={hideDialog} />
            <Button label="Refresh" icon="pi pi-check" className="p-button-text" onClick={() => { console.log(composicaoDataChart); setComposicaoDataChart(composicaoDataChart); } } />
        </>
    );
    const deleteComposicaoDialogFooter = (
        <>
            <Button label="No" icon="pi pi-times" className="p-button-text" onClick={hideDeleteComposicaoDialog} />
            <Button label="Yes" icon="pi pi-check" className="p-button-text" onClick={deleteComposicao} />
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
                        dataKey="codigoComposicao" loading={loading}
                        paginator rows={10} rowsPerPageOptions={[10, 15, 25]} 
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
                        globalFilter={globalFilter} globalFilterFields={['tabela', 'localidade', 'descricaoComposicao']} emptyMessage="No registers found." header={header} >                                                       
                        <Column rowReorder></Column>
                        <Column body={actionBodyTemplate}></Column>
                        <Column selectionMode="multiple" headerStyle={{ width: '3rem'}} hidden='true'></Column>                        
                        <Column field="codigoComposicao" header="id" sortable body={codeBodyTemplate} headerStyle={{ width: '5%', minWidth: '2rem' }}></Column>
                        <Column field="descricaoComposicao" header="Descrição" sortable body={descricaoBodyTemplate} headerStyle={{ width: '90%', minWidth: '20rem' }}></Column>
                        <Column field="unidade" header="Med" sortable body={medidaBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>                                                
                        <Column field="custoMaoObra" header="Mão Obra" sortable body={custoMaoObraBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                        <Column field="custoMaterial" header="Material" sortable body={custoMaterialBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                        <Column field="custoEquipamento" header="Equipa mento" sortable body={custoEquipamentoBodyTemplate} headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>    
                        <Column field="custoTotal" header="Total" body={custoTotalBodyTemplate} sortable headerStyle={{ width: '5%', minWidth: '5rem' }}></Column>
                    </DataTable>

                    <Dialog maximizable visible={composicaoDialog} style={{ width: '450px' }} header="Detalhes da Composição" modal className="p-fluid" footer={composicaoDialogFooter} onHide={hideDialog}>                        
                        <div className="composicaochart">
                            <div className="card">
                                <OrganizationChart value={composicaoDataChart} nodeTemplate={ (node) => nodeTemplate(node)} 
                                    selectionMode="multiple" className="composicaochart"></OrganizationChart>
                            </div>
                        </div>
                    </Dialog>

                    <Dialog visible={deleteComposicaoDialog} style={{ width: '450px' }} header="Confirm" modal footer={deleteComposicaoDialogFooter} onHide={hideDeleteComposicaoDialog}>
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