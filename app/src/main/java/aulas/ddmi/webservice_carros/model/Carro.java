package aulas.ddmi.webservice_carros.model;

/**
 * Created by vagner on 15/05/16.
 */

import java.io.Serializable;


public class Carro implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;

    private Long id;
    private String nome;
    private String desc;
    private String tipo;
    private String urlFoto;
    private String urlVideo;
    private String latitude;
    private String longitude;

    public Carro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Carro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", desc='" + desc + '\'' +
                ", tipo='" + tipo + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", urlVideo='" + urlVideo + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
