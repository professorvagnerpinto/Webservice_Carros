package aulas.ddmi.webservice_carros.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.activity.CarroActivity;
import aulas.ddmi.webservice_carros.model.Carro;
import aulas.ddmi.webservice_carros.service.RetrofitSetup;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by vagner on 15/05/16.
 */
public class CarroEdicaoFragment extends BaseFragment {

    private Carro carro; //uma instância da classe Carro com escopo global para utilização em membros da classe
    //componentes <-> objeto carro
    private ImageView imageViewFoto; //campo referente ao atributo url_foto
    private RadioButton rbClassicos, rbEsportivos, rbLuxo; //campos referente ao tipo do objeto carro
    private EditText editTextNome; //campo referente ao atributo nome do objeto carro
    private EditText editTextDescricao; //campo referente ao atributo descrição do objeto carro
    private EditText editTextLatitude;  //campo referente ao atributo latitude do objeto carro
    private EditText editTextLongitude; //campo referente ao atributo longitude do objeto carro
    private EditText editTextUrlVideo; //campo referente ao atributo url_video do objeto carro
    ProgressBar progressBarCard0; //progressBar do Card0, do container na imagem do carro

    //utilizado pelo fragment para redeber o objeto carro repassado pelo fragment chamador
    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); //informa ao sistema que o fragment irá adicionar botões na ActionBar

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //infla o layout
        View view = inflater.inflate(R.layout.fragment_edicaocarro, container, false);

        ((CarroActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_edicaocarro);  //um título para a janela

        //um log para depurar
        Log.d(TAG, "Dados do registro = " + carro);

        //carrega a imagem e controla o progressbar
        Log.d(TAG, "URL foto = " + carro.getUrlFoto()); //um log para depurar
        imageViewFoto = (ImageView) view.findViewById(R.id.imv_card0_fredicaocarro);
        if(carro.getUrlFoto() != null){
            progressBarCard0 = (ProgressBar) view.findViewById(R.id.pb_card0_fredicaocarro);
            Picasso.with(getContext()).load(carro.getUrlFoto()).fit().into(imageViewFoto, new Callback() {
                @Override
                public void onSuccess() {
                    progressBarCard0.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressBarCard0.setVisibility(View.VISIBLE);
                }
            });
        }
        imageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cria uma Intent
                //primeiro argumento: ação ACTION_PICK "escolha um item a partir dos dados e retorne o seu URI"
                //segundo argumento: refina a ação para arquivos de imagem, retornando um URI
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //inicializa uma Activity. Neste caso, uma que forneca acesso a galeria de imagens do dispositivo.
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 0);
            }
        });

        //carrega o tipo nos RadioButtons
        Log.d(TAG, "Tipo = " + carro.getTipo()); //um log para depurar
        rbClassicos = (RadioButton) view.findViewById(R.id.rbclassicos_card1_fredicaocarro);
        rbEsportivos = (RadioButton) view.findViewById(R.id.rbesportivos_card1_fredicaocarro);
        rbLuxo = (RadioButton) view.findViewById(R.id.rbluxo_card1_fredicaocarro);
        switch (carro.getTipo()) {
            case "classicos":
                rbClassicos.setChecked(true);
                break;
            case "esportivos":
                rbEsportivos.setChecked(true);
                break;
            default:
                rbLuxo.setChecked(true);
                break;
        }

        //carrega o nome e a descrição
        Log.d(TAG, "Nome = " + carro.getNome() + "\nDescrição = " + carro.getDesc()); //um log para depurar
        editTextNome = (EditText) view.findViewById(R.id.etNome_card2_fredicaocarro);
        editTextDescricao = (EditText) view.findViewById(R.id.etDescricao_card2_fredicaocarro);
        editTextNome.setText(carro.getNome());
        editTextDescricao.setText(carro.getDesc());

        //carrega a latitude e a longitude
        Log.d(TAG, "Latitude = " + carro.getLatitude() + "\nlongitude = " + carro.getLongitude()); //um log para depurar
        editTextLatitude = (EditText) view.findViewById(R.id.etlatitude_card3_fredicaocarro);
        editTextLongitude = (EditText) view.findViewById(R.id.etlongitude_card3_fredicaocarro);
        editTextLatitude.setText(carro.getLatitude());
        editTextLongitude.setText(carro.getLongitude());

        //carrega o vídeo
        Log.d(TAG, "URL vídeo = " + carro.getUrlVideo()); //um log para depurar
        editTextUrlVideo = (EditText) view.findViewById(R.id.etURLVideo__card4_fredicaocarro);
        if(carro.getUrlVideo() != null){
            editTextUrlVideo.setText(Uri.parse(carro.getUrlVideo()).toString());
        }
        editTextUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cria uma Intent
                //primeiro argumento: ação ACTION_PICK "escolha um item a partir dos dados e retorne o seu URI"
                //segundo argumento: refina a ação para arquivos de vídeo, retornando um URI
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                //inicializa uma Activity. Neste caso, uma que forneca acesso a galeria de imagens do dispositivo.
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 0);
            }
        });

        return view;
    }

    /*
        Infla o menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_edicaocarro, menu);
    }

    /*
        Trata eventos dos itens de menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_salvar:{
                //carrega os dados do formulário no objeto
                carro.setNome(editTextNome.getText().toString());
                carro.setDesc(editTextDescricao.getText().toString());
                carro.setLatitude(editTextLatitude.getText().toString());
                carro.setLongitude(editTextLongitude.getText().toString());
                if(rbClassicos.isChecked()){
                    carro.setTipo(getContext().getResources().getString(R.string.tipo_classicos));
                }else if(rbEsportivos.isChecked()){
                    carro.setTipo(getContext().getResources().getString(R.string.tipo_esportivos));
                }else {
                    carro.setTipo(getContext().getResources().getString(R.string.tipo_luxo));
                }
                //emite uma caixa de diálogo de processando
                showWait(getContext(), R.string.app_name, R.string.progressdialog_wait);
                //Faz uma call com a operação POST para o servidor
                Call call = new RetrofitSetup().getCarroService().alterar(carro);
                call.enqueue(new retrofit2.Callback() { //executa em uma Thread separada da main
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("tag", response.message());
                        //retira a caixa de diálogo de processando
                        dismissWait();
                        //faz aparecer uma caixa de diálogo confirmando a operação
                        alertOk(R.string.title_confirmacao, R.string.msg_realizadocomsucesso);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("tag", "Falha ao realizar a requisição POST.");
                    }
                });
                break;
            }
            case R.id.menuitem_excluir:{
                //emite uma caixa de diálogo de processando
                showWait(getContext(), R.string.app_name, R.string.progressdialog_wait);
                //Faz uma call com a operação POST para o servidor
                Call call = new RetrofitSetup().getCarroService().excluir(String.valueOf(carro.getId()));
                call.enqueue(new retrofit2.Callback() { //executa em uma Thread separada da main
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("tag", response.message());
                        //retira a caixa de diálogo de processando
                        dismissWait();
                        //faz aparecer uma caixa de diálogo confirmando a operação
                        alertOk(R.string.title_confirmacao, R.string.msg_realizadocomsucesso);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("tag", "Falha ao realizar a requisição POST.");
                    }
                });
                break;
            }
            case android.R.id.home:
                getActivity().finish();
                break;
        }

        return true;
    }

    /**
     * Método que recebe o retorno da Activity de galeria de imagens.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            Log.d(TAG, data.toString());
            Uri arquivoUri = data.getData(); //obtém o URI
            Log.d(TAG, "URI do arquivo: " + arquivoUri);
            if(arquivoUri.toString().contains("images")) {
                imageViewFoto.setImageURI(arquivoUri); //coloca a imagem no ImageView
                carro.setUrlFoto(arquivoUri.toString()); //armazena o Uri da imagem no objeto do modelo
            }else if(arquivoUri.toString().contains("video")) {
                //editTextUrlVideo.setText(arquivoUri.toString()); //coloca a URL do vídeo no EditText
                carro.setUrlVideo(arquivoUri.toString()); //armazena o Uri do vídeo no objeto do modelo
            }
        }
    }

}//fim classe externa
