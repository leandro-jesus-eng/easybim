import axios from 'axios';

export class ComposicoesService {

    getComposicoes(nameTable, localidade, dataPreco) {
        //return axios.get('assets/demo/data/composicoes.json').then(res => res.data.data);        
        return this.getDataFromApi('http://localhost:8080/REST/tabelacustosindices/composicoes', { params:{"nameTable": nameTable, "localidade": localidade, "dataPreco":dataPreco }} );        
    }

    getDataFromApi( addressHttp, params ) {
      return axios.get(addressHttp, params)
        .then((response) => {
            console.log(response.data);
            //console.log(response.status);
            //console.log(response.statusText);
            //console.log(response.headers);
            //console.log(response.config);            
            return response.data;
          })
        .catch((error) => {
            if (error.response) {
              // The request was made and the server responded with a status code
              // that falls out of the range of 2xx
              console.log(error.response.data);
              console.log(error.response.status);
              console.log(error.response.headers);
            } else if (error.request) {
              // The request was made but no response was received
              // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
              // http.ClientRequest in node.js
              console.log(error.request);
            } else {
              // Something happened in setting up the request that triggered an Error
              console.log('Error', error.message);
            }
            console.log(error.config);
          });
    }

    getNomeTabelas() {
        return this.getDataFromApi('http://localhost:8080/REST/tabelacustosindices/nomeTabelas');        
    }

    getLocalidades(nometabela) {
        return this.getDataFromApi('http://localhost:8080/REST/tabelacustosindices/localidades/'+nometabela);
    }

    getDataPrecos(nometabela) {
      return this.getDataFromApi('http://localhost:8080/REST/tabelacustosindices/dataPrecos/'+nometabela);
    }   
    
    getComposicaoById(idComposicao) {
      return this.getDataFromApi('http://localhost:8080/REST/tabelacustosindices/composicoes/'+idComposicao);
    } 
}