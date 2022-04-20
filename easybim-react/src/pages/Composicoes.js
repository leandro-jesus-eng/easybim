import React, { useState, useEffect, useRef } from "react";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dropdown } from "primereact/dropdown";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { FileUpload } from "primereact/fileupload";
import { Rating } from "primereact/rating";
import { Toolbar } from "primereact/toolbar";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { OrganizationChart } from "primereact/organizationchart";
import { OverlayPanel } from 'primereact/overlaypanel';

import { MenuOptions } from "../components/MenuOptions";
import { ComposicoesService } from "../service/ComposicoesService";
import "./ComposicaoChart.css";

const Composicoes = () => {
    let emptyProduct = {
        id: null,
        name: "",
        image: null,
        description: "",
        category: null,
        price: 0,
        quantity: 0,
        rating: 0,
        inventoryStatus: "INSTOCK",
    };

    const op = useRef(null);

    const [loading, setLoading] = useState(false);
    const [composicoes, setComposicoes] = useState(null);
    const [composicaoDialog, setComposicaoDialog] = useState(false);
    const [composicaoSelected, setComposicaoSelected] = useState(null);
    const [deleteComposicaoDialog, setDeleteComposicaoDialog] = useState(false);
    const [deleteProductsDialog, setDeleteProductsDialog] = useState(false);
    const [product, setProduct] = useState(emptyProduct);
    const [selectedRows, setSelectedRows] = useState(null);
    const [globalFilter, setGlobalFilter] = useState(null);
    const toast = useRef(null);
    const dt = useRef(null);
    const composicoesService = new ComposicoesService();

    const getComposicoes = (nomeTabela, localidade, dataPreco) => {        
        if (nomeTabela && localidade && dataPreco) {
            setLoading(true);
            composicoesService.getComposicoes(nomeTabela, localidade, dataPreco).then((data) => {
                setComposicoes(data);
                setLoading(false);
            });
        }
    };

    const [selectedNomeTabela, setSelectedNomeTabela] = useState(null);
    const [nomeTabelas, setNomeTabelas] = useState(null);
    const onNomeTabelaChange = (e) => {
        setSelectedNomeTabela(e.value);

        composicoesService.getLocalidades(e.value.name).then((data) => setLocalidades(data));
        composicoesService.getDataPrecos(e.value.name).then((data) => setDataPrecos(data));

        getComposicoes(e.value.name, selectedLocalidade, selectedDataPreco);
    };

    const [selectedLocalidade, setSelectedLocalidade] = useState(null);
    const [localidades, setLocalidades] = useState(null);
    const onLocalidadeChange = (e) => {
        setSelectedLocalidade(e.value);

        getComposicoes(selectedNomeTabela.name, e.value, selectedDataPreco);
    };

    const [selectedDataPreco, setSelectedDataPreco] = useState(null);
    const [dataPrecos, setDataPrecos] = useState(null);
    const onDataPrecoChange = (e) => {
        setSelectedDataPreco(e.value);

        getComposicoes(selectedNomeTabela.name, selectedLocalidade, e.value);
    };

    useEffect(() => {
        composicoesService.getNomeTabelas().then((data) => setNomeTabelas(data));
        //setComposicaoDataChart(composicaoDataChart);
    }, []);

    const formatCurrency = (value) => {
        return value.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
    };

    const openNew = () => {
        setComposicaoDialog(true);
    };

    const hideDialog = () => {
        setComposicaoDialog(false);
    };

    const hideDeleteComposicaoDialog = () => {
        setDeleteComposicaoDialog(false);
    };

    const hideDeleteProductsDialog = () => {
        setDeleteProductsDialog(false);
    };

    const deleteComposicao = () => {
        /*let _products = products.filter(val => val.id !== product.id);
        setProducts(_products);
        setDeleteComposicaoDialog(false);
        setProduct(emptyProduct);*/
        toast.current.show({ severity: "success", summary: "Successful", detail: "Composicao Deleted", life: 3000 });
    };

    const exportCSV = () => {
        dt.current.exportCSV();
    };

    const confirmDeleteSelected = () => {
        setDeleteProductsDialog(true);
    };

    const deleteSelectedProducts = () => {
        /*let _products = products.filter(val => !selectedProducts.includes(val));
        setProducts(_products);
        setDeleteProductsDialog(false);
        setSelectedProducts(null);
        toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Products Deleted', life: 3000 });*/
    };

    const onInputChange = (e, name) => {
        const val = (e.target && e.target.value) || "";
        let _product = { ...product };
        _product[`${name}`] = val;

        setProduct(_product);
    };

    const onInputNumberChange = (e, name) => {
        const val = e.value || 0;
        let _product = { ...product };
        _product[`${name}`] = val;

        setProduct(_product);
    };

    const leftToolbarTemplate = () => {
        return (
            <React.Fragment>
                <div className="my-2">
                    <Button label="New" icon="pi pi-plus" className="p-button-success mr-2" onClick={openNew} disabled={true} />
                    <Button label={"Delete " + (selectedRows && selectedRows.length > 0 ? "(" + selectedRows.length + ")" : "")} icon="pi pi-trash" className="p-button-danger" onClick={confirmDeleteSelected} disabled={!selectedRows || !selectedRows.length} />
                </div>
            </React.Fragment>
        );
    };

    const rightToolbarTemplate = () => {
        return (
            <React.Fragment>
                <FileUpload mode="basic" accept=".xls" maxFileSize={1000000} label="Import" chooseLabel="Import" className="mr-2 inline-block" />
                <Button label="Export" icon="pi pi-upload" className="p-button-help" onClick={exportCSV} />
            </React.Fragment>
        );
    };

    const codeBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Code</span>
                {rowData.codigoComposicao}
            </>
        );
    };

    const descricaoBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Name</span>
                {rowData.descricaoComposicao}
            </>
        );
    };

    const medidaBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Name</span>
                {rowData.unidade}
            </>
        );
    };

    const custoMaoObraBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoMaoObra)}
            </>
        );
    };

    const custoMaterialBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoMaterial)}
            </>
        );
    };

    const custoEquipamentoBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoEquipamento)}
            </>
        );
    };

    const custoTotalBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.custoTotal)}
            </>
        );
    };

    const nameBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Name</span>
                {rowData.name}
            </>
        );
    };

    const imageBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Image</span>
                <img src={`assets/demo/images/product/${rowData.image}`} alt={rowData.image} className="shadow-2" width="100" />
            </>
        );
    };

    const priceBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Price</span>
                {formatCurrency(rowData.price)}
            </>
        );
    };

    const categoryBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Category</span>
                {rowData.category}
            </>
        );
    };

    const ratingBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Reviews</span>
                <Rating value={rowData.rating} readonly cancel={false} />
            </>
        );
    };

    const statusBodyTemplate = (rowData) => {
        return (
            <>
                <span className="p-column-title">Status</span>
                <span className={`product-badge status-${rowData.inventoryStatus.toLowerCase()}`}>{rowData.inventoryStatus}</span>
            </>
        );
    };

    const dataTest = [
        {
            "children": [
                {
                    "children": [
                        {
                            "label": "36531",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": {
                                    "codigoItem": "36531",
                                    "descricaoItem": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRACAO 4 X 4, POTENCIA LIQUIDA 88 HP, PESO OPERACIONAL MINIMO DE 6674 KG, CAPACIDADE DA CARREGADEIRA DE 1,00 M3 E DA  RETROESCAVADEIRA MINIMA DE 0,26 M3, PROFUNDIDADE DE ESCAVACAO MAXIMA DE 4,37 M",
                                    "unidadeItem": "UN",
                                    "origemPrecoItem": "COEFICIENTE DE REPRESENTATIVIDADE",
                                    "precoUnitarioItem": 435365.82,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 0.0000076,
                                    "id": "625e3da00d9cbb6582fdd815",
                                    "custoTotalItem": 3.3
                                }
                            }
                        }
                    ],
                    "label": "88858",
                    "type": "COMPOSICAO",
                    "className": "p-composicao",
                    "expanded": true,
                    "data": {
                        "composicao": {
                            "id": "625e3da10d9cbb6582fddca8",
                            "tabelaCustosIndicesId": "625e3d9e0d9cbb6582fdc843",
                            "descricaoClasse": "desonerado",
                            "siglaClasse": "CHOR",
                            "descricaoTipo1": "COMPOSIÇÕES AUXILIARES",
                            "siglaTipo1": "329",
                            "codAgrupador": null,
                            "descricaoAgrupador": null,
                            "codigoComposicao": "88858",
                            "descricaoComposicao": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRAÇÃO 4X4, POTÊNCIA LÍQ. 88 HP, CAÇAMBA CARREG. CAP. MÍN. 1 M3, CAÇAMBA RETRO CAP. 0,26 M3, PESO OPERACIONAL MÍN. 6.674 KG, PROFUNDIDADE ESCAVAÇÃO MÁX. 4,37 M - JUROS. AF_06/2014",
                            "unidade": "H",
                            "origemPreco": "COEFICIENTE DE REPRESENTATIVIDADE",
                            "custoTotal": 3.3,
                            "custoMaoObra": 0,
                            "percentualMaoObra": 0,
                            "custoMaterial": 0,
                            "percentualMaterial": 0,
                            "custoEquipamento": 3.3,
                            "percentualEquipamento": 100,
                            "custoServicosTerceiros": 0,
                            "percentualServicosTerceiros": 0,
                            "custosOutros": 0,
                            "percentualOutros": 0,
                            "percentualAtribuidoSaoPaulo": 0,
                            "vinculo": "CAIXA REFERENCIAL",
                            "itensComposicao": [
                                {
                                    "codigoItem": "36531",
                                    "descricaoItem": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRACAO 4 X 4, POTENCIA LIQUIDA 88 HP, PESO OPERACIONAL MINIMO DE 6674 KG, CAPACIDADE DA CARREGADEIRA DE 1,00 M3 E DA  RETROESCAVADEIRA MINIMA DE 0,26 M3, PROFUNDIDADE DE ESCAVACAO MAXIMA DE 4,37 M",
                                    "unidadeItem": "UN",
                                    "origemPrecoItem": "COEFICIENTE DE REPRESENTATIVIDADE",
                                    "precoUnitarioItem": 435365.82,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 0.0000076,
                                    "id": "625e3da00d9cbb6582fdd815",
                                    "custoTotalItem": 3.3
                                }
                            ]
                        }
                    }
                },
                {
                    "children": [
                        {
                            "label": "36531",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": {
                                    "codigoItem": "36531",
                                    "descricaoItem": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRACAO 4 X 4, POTENCIA LIQUIDA 88 HP, PESO OPERACIONAL MINIMO DE 6674 KG, CAPACIDADE DA CARREGADEIRA DE 1,00 M3 E DA  RETROESCAVADEIRA MINIMA DE 0,26 M3, PROFUNDIDADE DE ESCAVACAO MAXIMA DE 4,37 M",
                                    "unidadeItem": "UN",
                                    "origemPrecoItem": "COEFICIENTE DE REPRESENTATIVIDADE",
                                    "precoUnitarioItem": 435365.82,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 0.000056,
                                    "id": "625e3da00d9cbb6582fdd815",
                                    "custoTotalItem": 24.38
                                }
                            }
                        }
                    ],
                    "label": "88857",
                    "type": "COMPOSICAO",
                    "className": "p-composicao",
                    "expanded": true,
                    "data": {
                        "composicao": {
                            "id": "625e3da10d9cbb6582fddca7",
                            "tabelaCustosIndicesId": "625e3d9e0d9cbb6582fdc843",
                            "descricaoClasse": "desonerado",
                            "siglaClasse": "CHOR",
                            "descricaoTipo1": "COMPOSIÇÕES AUXILIARES",
                            "siglaTipo1": "329",
                            "codAgrupador": null,
                            "descricaoAgrupador": null,
                            "codigoComposicao": "88857",
                            "descricaoComposicao": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRAÇÃO 4X4, POTÊNCIA LÍQ. 88 HP, CAÇAMBA CARREG. CAP. MÍN. 1 M3, CAÇAMBA RETRO CAP. 0,26 M3, PESO OPERACIONAL MÍN. 6.674 KG, PROFUNDIDADE ESCAVAÇÃO MÁX. 4,37 M - DEPRECIAÇÃO. AF_06/2014",
                            "unidade": "H",
                            "origemPreco": "COEFICIENTE DE REPRESENTATIVIDADE",
                            "custoTotal": 24.38,
                            "custoMaoObra": 0,
                            "percentualMaoObra": 0,
                            "custoMaterial": 0,
                            "percentualMaterial": 0,
                            "custoEquipamento": 24.38,
                            "percentualEquipamento": 100,
                            "custoServicosTerceiros": 0,
                            "percentualServicosTerceiros": 0,
                            "custosOutros": 0,
                            "percentualOutros": 0,
                            "percentualAtribuidoSaoPaulo": 0,
                            "vinculo": "CAIXA REFERENCIAL",
                            "itensComposicao": [
                                {
                                    "codigoItem": "36531",
                                    "descricaoItem": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRACAO 4 X 4, POTENCIA LIQUIDA 88 HP, PESO OPERACIONAL MINIMO DE 6674 KG, CAPACIDADE DA CARREGADEIRA DE 1,00 M3 E DA  RETROESCAVADEIRA MINIMA DE 0,26 M3, PROFUNDIDADE DE ESCAVACAO MAXIMA DE 4,37 M",
                                    "unidadeItem": "UN",
                                    "origemPrecoItem": "COEFICIENTE DE REPRESENTATIVIDADE",
                                    "precoUnitarioItem": 435365.82,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 0.000056,
                                    "id": "625e3da00d9cbb6582fdd815",
                                    "custoTotalItem": 24.38
                                }
                            ]
                        }
                    }
                },
                {
                    "children": [
                        {
                            "label": "4234",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": { "insumo": { "codigoItem": "4234", "descricaoItem": "OPERADOR DE ESCAVADEIRA", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 18.71, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3d9e0d9cbb6582fdc905", "custoTotalItem": 18.71 } }
                        },
                        {
                            "label": "37370",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": {
                                    "codigoItem": "37370",
                                    "descricaoItem": "ALIMENTACAO - HORISTA (COLETADO CAIXA)",
                                    "unidadeItem": "H",
                                    "origemPrecoItem": "COLETADO",
                                    "precoUnitarioItem": 2.48,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 1,
                                    "id": "625e3d9e0d9cbb6582fdc91f",
                                    "custoTotalItem": 2.48
                                }
                            }
                        },
                        {
                            "label": "37371",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": {
                                    "codigoItem": "37371",
                                    "descricaoItem": "TRANSPORTE - HORISTA (COLETADO CAIXA)",
                                    "unidadeItem": "H",
                                    "origemPrecoItem": "COLETADO",
                                    "precoUnitarioItem": 0.93,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 1,
                                    "id": "625e3da10d9cbb6582fdda9a",
                                    "custoTotalItem": 0.93
                                }
                            }
                        },
                        {
                            "label": "37372",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": { "codigoItem": "37372", "descricaoItem": "EXAMES - HORISTA (COLETADO CAIXA)", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 0.81, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3d9f0d9cbb6582fdd0c2", "custoTotalItem": 0.81 }
                            }
                        },
                        {
                            "label": "37373",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": { "codigoItem": "37373", "descricaoItem": "SEGURO - HORISTA (COLETADO CAIXA)", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 0.06, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3da10d9cbb6582fdd852", "custoTotalItem": 0.06 }
                            }
                        },
                        {
                            "label": "43464",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": {
                                    "codigoItem": "43464",
                                    "descricaoItem": "FERRAMENTAS - FAMILIA OPERADOR ESCAVADEIRA - HORISTA (ENCARGOS COMPLEMENTARES - COLETADO CAIXA)",
                                    "unidadeItem": "H",
                                    "origemPrecoItem": "COLETADO",
                                    "precoUnitarioItem": 0.01,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 1,
                                    "id": "625e3d9f0d9cbb6582fdd101",
                                    "custoTotalItem": 0.01
                                }
                            }
                        },
                        {
                            "label": "43488",
                            "type": "INSUMO",
                            "className": "p-insumo",
                            "expanded": true,
                            "data": {
                                "insumo": {
                                    "codigoItem": "43488",
                                    "descricaoItem": "EPI - FAMILIA OPERADOR ESCAVADEIRA - HORISTA (ENCARGOS COMPLEMENTARES - COLETADO CAIXA)",
                                    "unidadeItem": "H",
                                    "origemPrecoItem": "COLETADO",
                                    "precoUnitarioItem": 0.76,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 1,
                                    "id": "625e3d9f0d9cbb6582fdd076",
                                    "custoTotalItem": 0.76
                                }
                            }
                        },
                        {
                            "children": [
                                {
                                    "label": "4234",
                                    "type": "INSUMO",
                                    "className": "p-insumo",
                                    "expanded": true,
                                    "data": {
                                        "insumo": { "codigoItem": "4234", "descricaoItem": "OPERADOR DE ESCAVADEIRA", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 18.71, "tipoItem": "INSUMO", "coeficienteItem": 0.0094, "id": "625e3d9e0d9cbb6582fdc905", "custoTotalItem": 0.17 }
                                    }
                                }
                            ],
                            "label": "95357",
                            "type": "COMPOSICAO",
                            "className": "p-composicao",
                            "expanded": true,
                            "data": {
                                "composicao": {
                                    "id": "625e3da10d9cbb6582fddca5",
                                    "tabelaCustosIndicesId": "625e3d9e0d9cbb6582fdc843",
                                    "descricaoClasse": "desonerado",
                                    "siglaClasse": "SEDI",
                                    "descricaoTipo1": "OUTROS",
                                    "siglaTipo1": "318",
                                    "codAgrupador": null,
                                    "descricaoAgrupador": null,
                                    "codigoComposicao": "95357",
                                    "descricaoComposicao": "CURSO DE CAPACITAÇÃO PARA OPERADOR DE ESCAVADEIRA (ENCARGOS COMPLEMENTARES) - HORISTA",
                                    "unidade": "H",
                                    "origemPreco": "COLETADO",
                                    "custoTotal": 0.17,
                                    "custoMaoObra": 0.17,
                                    "percentualMaoObra": 100,
                                    "custoMaterial": 0,
                                    "percentualMaterial": 0,
                                    "custoEquipamento": 0,
                                    "percentualEquipamento": 0,
                                    "custoServicosTerceiros": 0,
                                    "percentualServicosTerceiros": 0,
                                    "custosOutros": 0,
                                    "percentualOutros": 0,
                                    "percentualAtribuidoSaoPaulo": 0,
                                    "vinculo": "ENCARGOS COMPLEMENTARES REFERENCIAL",
                                    "itensComposicao": [
                                        { "codigoItem": "4234", "descricaoItem": "OPERADOR DE ESCAVADEIRA", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 18.71, "tipoItem": "INSUMO", "coeficienteItem": 0.0094, "id": "625e3d9e0d9cbb6582fdc905", "custoTotalItem": 0.17 }
                                    ]
                                }
                            }
                        }
                    ],
                    "label": "88294",
                    "type": "COMPOSICAO",
                    "className": "p-composicao",
                    "expanded": true,
                    "data": {
                        "composicao": {
                            "id": "625e3da10d9cbb6582fddca6",
                            "tabelaCustosIndicesId": "625e3d9e0d9cbb6582fdc843",
                            "descricaoClasse": "desonerado",
                            "siglaClasse": "SEDI",
                            "descricaoTipo1": "OUTROS",
                            "siglaTipo1": "318",
                            "codAgrupador": null,
                            "descricaoAgrupador": null,
                            "codigoComposicao": "88294",
                            "descricaoComposicao": "OPERADOR DE ESCAVADEIRA COM ENCARGOS COMPLEMENTARES",
                            "unidade": "H",
                            "origemPreco": "COLETADO",
                            "custoTotal": 23.93,
                            "custoMaoObra": 18.88,
                            "percentualMaoObra": 78.8967823,
                            "custoMaterial": 5.05,
                            "percentualMaterial": 21.1032177,
                            "custoEquipamento": 0,
                            "percentualEquipamento": 0,
                            "custoServicosTerceiros": 0,
                            "percentualServicosTerceiros": 0,
                            "custosOutros": 0,
                            "percentualOutros": 0,
                            "percentualAtribuidoSaoPaulo": 0,
                            "vinculo": "ENCARGOS COMPLEMENTARES REFERENCIAL",
                            "itensComposicao": [
                                { "codigoItem": "4234", "descricaoItem": "OPERADOR DE ESCAVADEIRA", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 18.71, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3d9e0d9cbb6582fdc905", "custoTotalItem": 18.71 },
                                { "codigoItem": "37370", "descricaoItem": "ALIMENTACAO - HORISTA (COLETADO CAIXA)", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 2.48, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3d9e0d9cbb6582fdc91f", "custoTotalItem": 2.48 },
                                { "codigoItem": "37371", "descricaoItem": "TRANSPORTE - HORISTA (COLETADO CAIXA)", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 0.93, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3da10d9cbb6582fdda9a", "custoTotalItem": 0.93 },
                                { "codigoItem": "37372", "descricaoItem": "EXAMES - HORISTA (COLETADO CAIXA)", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 0.81, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3d9f0d9cbb6582fdd0c2", "custoTotalItem": 0.81 },
                                { "codigoItem": "37373", "descricaoItem": "SEGURO - HORISTA (COLETADO CAIXA)", "unidadeItem": "H", "origemPrecoItem": "COLETADO", "precoUnitarioItem": 0.06, "tipoItem": "INSUMO", "coeficienteItem": 1, "id": "625e3da10d9cbb6582fdd852", "custoTotalItem": 0.06 },
                                {
                                    "codigoItem": "43464",
                                    "descricaoItem": "FERRAMENTAS - FAMILIA OPERADOR ESCAVADEIRA - HORISTA (ENCARGOS COMPLEMENTARES - COLETADO CAIXA)",
                                    "unidadeItem": "H",
                                    "origemPrecoItem": "COLETADO",
                                    "precoUnitarioItem": 0.01,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 1,
                                    "id": "625e3d9f0d9cbb6582fdd101",
                                    "custoTotalItem": 0.01
                                },
                                {
                                    "codigoItem": "43488",
                                    "descricaoItem": "EPI - FAMILIA OPERADOR ESCAVADEIRA - HORISTA (ENCARGOS COMPLEMENTARES - COLETADO CAIXA)",
                                    "unidadeItem": "H",
                                    "origemPrecoItem": "COLETADO",
                                    "precoUnitarioItem": 0.76,
                                    "tipoItem": "INSUMO",
                                    "coeficienteItem": 1,
                                    "id": "625e3d9f0d9cbb6582fdd076",
                                    "custoTotalItem": 0.76
                                },
                                {
                                    "codigoItem": "95357",
                                    "descricaoItem": "CURSO DE CAPACITAÇÃO PARA OPERADOR DE ESCAVADEIRA (ENCARGOS COMPLEMENTARES) - HORISTA",
                                    "unidadeItem": "H",
                                    "origemPrecoItem": "COLETADO",
                                    "precoUnitarioItem": 0.17,
                                    "tipoItem": "COMPOSICAO",
                                    "coeficienteItem": 1,
                                    "id": "625e3da10d9cbb6582fddca5",
                                    "custoTotalItem": 0.17
                                }
                            ]
                        }
                    }
                }
            ],
            "label": "5679",
            "type": "COMPOSICAO",
            "className": "p-composicao",
            "expanded": true,
            "data": {
                "composicao": {
                    "id": "625e3da10d9cbb6582fddcaa",
                    "tabelaCustosIndicesId": "625e3d9e0d9cbb6582fdc843",
                    "descricaoClasse": "desonerado",
                    "siglaClasse": "CHOR",
                    "descricaoTipo1": "CUSTO HORÁRIO IMPRODUTIVO DIURNO",
                    "siglaTipo1": "327",
                    "codAgrupador": null,
                    "descricaoAgrupador": null,
                    "codigoComposicao": "5679",
                    "descricaoComposicao": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRAÇÃO 4X4, POTÊNCIA LÍQ. 88 HP, CAÇAMBA CARREG. CAP. MÍN. 1 M3, CAÇAMBA RETRO CAP. 0,26 M3, PESO OPERACIONAL MÍN. 6.674 KG, PROFUNDIDADE ESCAVAÇÃO MÁX. 4,37 M - CHI DIURNO. AF_06/2014",
                    "unidade": "CHI",
                    "origemPreco": "COEFICIENTE DE REPRESENTATIVIDADE",
                    "custoTotal": 51.61,
                    "custoMaoObra": 18.88,
                    "percentualMaoObra": 36.5820577,
                    "custoMaterial": 5.05,
                    "percentualMaterial": 9.7849254,
                    "custoEquipamento": 27.68,
                    "percentualEquipamento": 53.6330169,
                    "custoServicosTerceiros": 0,
                    "percentualServicosTerceiros": 0,
                    "custosOutros": 0,
                    "percentualOutros": 0,
                    "percentualAtribuidoSaoPaulo": 0,
                    "vinculo": "CAIXA REFERENCIAL",
                    "itensComposicao": [
                        {
                            "codigoItem": "88294",
                            "descricaoItem": "OPERADOR DE ESCAVADEIRA COM ENCARGOS COMPLEMENTARES",
                            "unidadeItem": "H",
                            "origemPrecoItem": "COLETADO",
                            "precoUnitarioItem": 23.93,
                            "tipoItem": "COMPOSICAO",
                            "coeficienteItem": 1,
                            "id": "625e3da10d9cbb6582fddca6",
                            "custoTotalItem": 23.93
                        },
                        {
                            "codigoItem": "88857",
                            "descricaoItem": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRAÇÃO 4X4, POTÊNCIA LÍQ. 88 HP, CAÇAMBA CARREG. CAP. MÍN. 1 M3, CAÇAMBA RETRO CAP. 0,26 M3, PESO OPERACIONAL MÍN. 6.674 KG, PROFUNDIDADE ESCAVAÇÃO MÁX. 4,37 M - DEPRECIAÇÃO. AF_06/2014",
                            "unidadeItem": "H",
                            "origemPrecoItem": "COEFICIENTE DE REPRESENTATIVIDADE",
                            "precoUnitarioItem": 24.38,
                            "tipoItem": "COMPOSICAO",
                            "coeficienteItem": 1,
                            "id": "625e3da10d9cbb6582fddca7",
                            "custoTotalItem": 24.38
                        },
                        {
                            "codigoItem": "88858",
                            "descricaoItem": "RETROESCAVADEIRA SOBRE RODAS COM CARREGADEIRA, TRAÇÃO 4X4, POTÊNCIA LÍQ. 88 HP, CAÇAMBA CARREG. CAP. MÍN. 1 M3, CAÇAMBA RETRO CAP. 0,26 M3, PESO OPERACIONAL MÍN. 6.674 KG, PROFUNDIDADE ESCAVAÇÃO MÁX. 4,37 M - JUROS. AF_06/2014",
                            "unidadeItem": "H",
                            "origemPrecoItem": "COEFICIENTE DE REPRESENTATIVIDADE",
                            "precoUnitarioItem": 3.3,
                            "tipoItem": "COMPOSICAO",
                            "coeficienteItem": 1,
                            "id": "625e3da10d9cbb6582fddca8",
                            "custoTotalItem": 3.3
                        }
                    ]
                }
            }
        }
    ];

    const [composicaoDataChart, setComposicaoDataChart] = useState([]);
    let dataAux = [];
    const getComposicaoChart = (composicao) => {
        dataAux = [];        
        getComposicaoChartRecursive(composicao, dataAux);
    };
    const  getComposicaoChartRecursive = (composicao, data) => {
        if (!composicao) return;

        data.push({
            children: [],
            label: composicao.codigoComposicao,
            type: "COMPOSICAO",
            className: "p-composicao",
            expanded: true,
            data: { composicao: composicao },
        });
        let lastIndex = data.length - 1;

        composicao.itensComposicao.forEach((itemComposicao) => {
            if (itemComposicao.tipoItem == "COMPOSICAO") {
                // TODO buscar a composicao no servidor
                composicoesService.getComposicaoById(itemComposicao.id).then((comp) => getComposicaoChartRecursive(comp, data[lastIndex].children));
            } else {
                data[lastIndex].children.push({
                    label: itemComposicao.codigoItem,
                    type: "INSUMO",
                    className: "p-insumo",
                    expanded: true,
                    data: { insumo: itemComposicao },
                });
            }
        });        
        var copia = JSON.parse(JSON.stringify(dataAux));
        setComposicaoDataChart(copia);
    };

    const nodeTemplate = (node) => {
        //style={{ width: '200px' }}
        if (node.type === "COMPOSICAO") {
            return (
                <div onClick={(e) => op[node.data.composicao.id].current.toggle(e)}>
                    <div className="node-header">{node.label}</div>
                    <div className="node-content">
                        <div>{node.data.composicao.descricaoComposicao.split(' ')[0]}...</div>                        
                    </div>

                    <OverlayPanel ref={op[node.data.composicao.id]} showCloseIcon dismissable>
                        <div>Descrição: {node.data.composicao.descricaoComposicao}</div>
                        <div>Custo Mão de Obra: {node.data.composicao.custoMaoObra}</div>                            
                        <div>Custo Material: {node.data.composicao.custoMaterial}</div>                            
                        <div>Custo Equipamento: {node.data.composicao.custoEquipamento}</div>                            
                        <div>Custo Serviços Terceiros: {node.data.composicao.custoServicosTerceiros}</div>         
                        <div>Outros Custos: {node.data.composicao.custosOutros}</div>       
                        <div>Custo Total: {node.data.composicao.custoTotal}</div> 
                    </OverlayPanel>
                </div>
            );
        } else { // INSUMO
            return (
                <div onClick={(e) => op[999].current.toggle(e)}>
                    <div className="node-header-insumo">{node.label}</div>
                    <div className="node-content-insumo">
                        {node.data.insumo.descricaoItem.split(' ')[0]}...
                    </div>

                    <OverlayPanel ref={op[999]}>
                        <div>Descrição: {node.data.insumo.descricaoItem}</div>
                        <div>Preço Unitário: {node.data.insumo.precoUnitarioItem}</div>
                        <div>Coeficiente: {node.data.insumo.coeficienteItem}</div>
                        <div>Custo Total: {node.data.insumo.custoTotalItem}</div>
                    </OverlayPanel>
                </div>
                
            );
        }
    };

    const actionBodyTemplate = (composicao) => {
        const itemsMenuComposicao = [
            {
                label: "Árvore de fatores",
                icon: "pi pi-fw pi-sitemap",
                command: () => {
                    setComposicaoSelected(composicao);
                    getComposicaoChart(composicao);
                    setComposicaoDialog(true);
                },
            },
            {
                separator: true,
            },
            {
                label: "Criar composição própria",
                icon: "pi pi-fw pi-sign-in",
                command: () => {
                    alert(composicao.codigoComposicao);
                },
            },
            {
                label: "Remover composição",
                icon: "pi pi-fw pi-trash",
                command: () => {
                    alert(composicao.codigoComposicao);
                },
            },
        ];

        return (
            <>
                <MenuOptions items={itemsMenuComposicao}></MenuOptions>
            </>
        );
    };

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
    };

    const composicaoDialogFooter = (
        <>
            <Button label="Close" icon="pi pi-times" className="p-button-text" onClick={hideDialog} />            
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

                    <DataTable
                        ref={dt}
                        size="small"
                        responsiveLayout="scroll"
                        className="datatable-responsive"
                        value={composicoes}
                        selection={selectedRows}
                        onSelectionChange={(e) => setSelectedRows(e.value)}
                        dataKey="codigoComposicao"
                        loading={loading}
                        paginator
                        rows={10}
                        rowsPerPageOptions={[10, 15, 25]}
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
                        globalFilter={globalFilter}
                        globalFilterFields={["tabela", "localidade", "descricaoComposicao"]}
                        emptyMessage="No registers found."
                        header={header}
                    >
                        <Column rowReorder></Column>
                        <Column body={actionBodyTemplate}></Column>
                        <Column selectionMode="multiple" headerStyle={{ width: "3rem" }} hidden="true"></Column>
                        <Column field="codigoComposicao" header="id" sortable body={codeBodyTemplate} headerStyle={{ width: "5%", minWidth: "2rem" }}></Column>
                        <Column field="descricaoComposicao" header="Descrição" sortable body={descricaoBodyTemplate} headerStyle={{ width: "90%", minWidth: "20rem" }}></Column>
                        <Column field="unidade" header="Med" sortable body={medidaBodyTemplate} headerStyle={{ width: "5%", minWidth: "5rem" }}></Column>
                        <Column field="custoMaoObra" header="Mão Obra" sortable body={custoMaoObraBodyTemplate} headerStyle={{ width: "5%", minWidth: "5rem" }}></Column>
                        <Column field="custoMaterial" header="Material" sortable body={custoMaterialBodyTemplate} headerStyle={{ width: "5%", minWidth: "5rem" }}></Column>
                        <Column field="custoEquipamento" header="Equipa mento" sortable body={custoEquipamentoBodyTemplate} headerStyle={{ width: "5%", minWidth: "5rem" }}></Column>
                        <Column field="custoTotal" header="Total" body={custoTotalBodyTemplate} sortable headerStyle={{ width: "5%", minWidth: "5rem" }}></Column>
                    </DataTable>

                    <Dialog maximizable visible={composicaoDialog} header="Detalhes da Composição" modal className="p-fluid" footer={composicaoDialogFooter} onHide={hideDialog}>
                        <div className="composicaochart">
                            <div className="card">
                                <OrganizationChart value={composicaoDataChart} nodeTemplate={(node) => nodeTemplate(node)} selectionMode="multiple" className="composicaochart"></OrganizationChart>
                            </div>
                        </div>
                    </Dialog>

                    <Dialog visible={deleteComposicaoDialog} style={{ width: "450px" }} header="Confirm" modal footer={deleteComposicaoDialogFooter} onHide={hideDeleteComposicaoDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: "2rem" }} />
                            {product && (
                                <span>
                                    Are you sure you want to delete <b>{product.name}</b>?
                                </span>
                            )}
                        </div>
                    </Dialog>

                    <Dialog visible={deleteProductsDialog} style={{ width: "450px" }} header="Confirm" modal footer={deleteProductsDialogFooter} onHide={hideDeleteProductsDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: "2rem" }} />
                            {product && <span>Are you sure you want to delete the selected products?</span>}
                        </div>
                    </Dialog>
                </div>
            </div>
        </div>
    );
};

const comparisonFn = function (prevProps, nextProps) {
    return prevProps.location.pathname === nextProps.location.pathname;
};

export default React.memo(Composicoes, comparisonFn);
