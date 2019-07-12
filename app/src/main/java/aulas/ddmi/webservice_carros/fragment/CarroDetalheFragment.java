package aulas.ddmi.webservice_carros.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.activity.CarroActivity;
import aulas.ddmi.webservice_carros.model.Carro;

/**
 * Created by vagner on 28/05/16.
 */
public class CarroDetalheFragment extends BaseFragment implements OnMapReadyCallback {

    private Carro carro; //uma instância da classe Carro com escopo global para utilização em membros da classe
    //componentes <-> objeto carro
    private RadioButton rbClassicos, rbEsportivos, rbLuxo; //campos referente ao tipo do objeto carro
    private TextView textViewNome; //campo referente ao atributo nome do objeto carro
    private TextView textViewDescricao; //campo referente ao atributo descrição do objeto carro
    private TextView textViewLatitude;  //campo referente ao atributo latitude do objeto carro
    private TextView textViewLongitude; //campo referente ao atributo longitude do objeto carro
    private ImageView imageView; //container para a foto do carro
    private VideoView videoView; //container para exibir o vídeo do carro
    ProgressBar progressBarCard0; //progressBar do Card0, do container na imagem do carro
    ProgressBar progressBarCard4; //progressBar do Card4, do container na vídeo do carro

    //utilizado pela Activity para repassar o objeto carro para este fragmento
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
        View view = inflater.inflate(R.layout.fragment_detalhecarro, container, false);

        ((CarroActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_detalhecarro); //um título para a janela

        //um log para depurar
        Log.d(TAG, "Dados do registro = " + carro);

        //carrega a imagem e controla o progressbar
        imageView = (ImageView) view.findViewById(R.id.imv_card0_frdetalhecarro);
        if(carro.getUrlFoto() != null){
            Log.d(TAG, "URL foto = " + carro.getUrlFoto()); //um log para depurar
            progressBarCard0 = (ProgressBar) view.findViewById(R.id.pb_card0_frdetalhecarro);
            Picasso.with(getContext()).load(carro.getUrlFoto()).fit().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBarCard0.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressBarCard0.setVisibility(View.VISIBLE);
                }
            });
        }else{
            imageView.setImageResource(R.drawable.car_background);
        }


        //carrega o tipo nos RadioButtons
        Log.d(TAG, "Tipo = " + carro.getTipo()); //um log para depurar
        rbClassicos = (RadioButton) view.findViewById(R.id.rbclassicos_card1_detalhecarro);
        rbEsportivos = (RadioButton) view.findViewById(R.id.rbespotivos_card1_detalhecarro);
        rbLuxo = (RadioButton) view.findViewById(R.id.rbluxo_card1_detalhecarro);
        if (carro.getTipo().equals(getContext().getResources().getString(R.string.tipo_classicos))) {
            rbClassicos.setChecked(true);
        } else if (carro.getTipo().equals(getContext().getResources().getString(R.string.tipo_esportivos))) {
            rbEsportivos.setChecked(true);
        } else {
            rbLuxo.setChecked(true);
        }

        //carrega o nome e a descrição
        Log.d(TAG, "Nome = " + carro.getNome() + "\nDescrição = " + carro.getDesc()); //um log para depurar
        textViewNome = (TextView) view.findViewById(R.id.tvNome_card0_frdetalhecarro);
        textViewDescricao = (TextView) view.findViewById(R.id.tvDescricao_card2_frdetalhecarro);
        textViewNome.setText(carro.getNome());
        textViewDescricao.setText(carro.getDesc());

        //carrega a latitude e a longitude
        textViewLatitude = (TextView) view.findViewById(R.id.tvLatitude_card2_frdetalhecarro);
        textViewLongitude = (TextView) view.findViewById(R.id.tvLongitude_card2_frdetalhecarro);
        textViewLatitude.setText(carro.getLatitude());
        textViewLongitude.setText(carro.getLongitude());
        Log.d(TAG, "Latitude = " + carro.getLatitude() + "\nlongitude = " + carro.getLongitude()); //um log para depurar
        // Mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Inicia o Google Maps dentro do fragment
        mapFragment.getMapAsync(this);

        //carrega  o video
        //Só suporta os formatos aceitos nativamente pelo Android
        videoView = (VideoView) view.findViewById(R.id.videoView_card4_frdetalhecarro);
        progressBarCard4 = (ProgressBar) view.findViewById(R.id.pb_card4_fredetalhecarro);
        final ImageView imageViewPlayVideo = (ImageView) view.findViewById(R.id.imageView_card4_fradetalhecarro);
        imageViewPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "URL Vídeo = " + carro.getUrlVideo()); //um log para depurar
                if(carro.getUrlVideo() != null){
                    imageViewPlayVideo.setVisibility(View.INVISIBLE);
                    videoView.setMediaController(new MediaController(getContext()));
                    videoView.setVideoURI(Uri.parse(carro.getUrlVideo()));
                    videoView.start();
                }else{
                    toast(R.string.msg_toast_naohavideo);
                }
            }
        });

        //isso deve ser ajustado na próxima versão
        progressBarCard4.setVisibility(View.INVISIBLE);



        return view;
    }

    /*
        Infla o menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_detalhecarro, menu);
    }

    /*
        Trata eventos dos itens de menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_editar: {
                //Substitui o Fragmento no container R.id.fragment_container, componente do layout content_main.xml
                CarroEdicaoFragment edicaocarroEdicaoFragment = new CarroEdicaoFragment();
                edicaocarroEdicaoFragment.setCarro(this.carro);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, edicaocarroEdicaoFragment).commit();
                break;
            }
            case android.R.id.home:
                getActivity().finish();
                break;
        }

        return false;
    }

    /*
        Manipula o fragmento do Maps.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (carro != null && googleMap != null) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            googleMap.setMyLocationEnabled(true); //habilita o botão minha localização no mapa


            // parse das coordenadas de String para Double
            double lat = Double.parseDouble(carro.getLatitude());
            double lng = Double.parseDouble(carro.getLongitude());

            if (lat != 0 && lng != 0) {
                LatLng location = new LatLng(lat, lng); // Cria o objeto LatLng com a coordenada da fábrica

                Log.d(TAG, "Lat = " + lat + " Long = " + lng);

                // Posiciona o mapa na coordenada da fábrica (zoom = 13)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                // Adiciona um marcador para o objeto location
                googleMap.addMarker(new MarkerOptions()
                        .title(carro.getNome())
                        .snippet("Local de uma de suas fábricas.")
                        .position(location));

                // Objeto para controlar o zoom da câmera
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);
                googleMap.moveCamera(update);

                googleMap.animateCamera(update, 2000, null); //anima o zoom

                // Marcador no local da fábrica
                googleMap.addMarker(new MarkerOptions()
                        .title(carro.getNome())
                        .snippet(carro.getDesc())
                        .position(location));
            }

            // Tipo do mapa: MAP_TYPE_NORMAL,
            // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
    }

}//fim carro
