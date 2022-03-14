import axios from 'axios';

export class ComposicoesService {

    getComposicoes() {
        return axios.get('assets/demo/data/composicoes.json').then(res => res.data.data);
    }
}