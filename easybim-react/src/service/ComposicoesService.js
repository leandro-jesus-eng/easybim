import axios from 'axios';

export class ComposicoesService {

    getComposicoes() {
        return axios.get('assets/demo/data/composicoes.json').then(res => res.data.data);
    }

    getNomeTabelas() {
        return axios.get('http://localhost:8080/REST/tabelacustosindices/nomeTabelas').then(res => res.data.data);
    }
}