package aulas.ddmi.webservice_carros.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Callback;

import com.squareup.picasso.Picasso;

import java.util.List;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.model.Carro;


/**
 * Esta classe realiza a adaptação dos dados entre a RecyclerView <-> List.
 * Neste projeto a List está sendo alimentada com dados oriundos de um webservice, via JSON.
 * @author Vagner Pinto da Silva
 */
public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarrosViewHolder> {
    protected static final String TAG = "web_adapter";
    private final List<Carro> carros;
    private final Context context;

    private CarroOnClickListener carroOnClickListener;

    public CarroAdapter(Context context, List<Carro> carros, CarroOnClickListener carroOnClickListener) {
        this.context = context;
        this.carros = carros;
        this.carroOnClickListener = carroOnClickListener;
    }

    @Override
    public int getItemCount() {
        return this.carros != null ? this.carros.size() : 0;
    }

    @Override
    public CarrosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_carros, viewGroup, false);

        // Cria o ViewHolder
        CarrosViewHolder holder = new CarrosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CarrosViewHolder holder, final int position) {
        // Atualiza a view
        Carro c = carros.get(position);

        holder.tNome.setText(c.getNome());
        holder.progress.setVisibility(View.VISIBLE);

        Picasso.with(context).load(c.getUrlFoto()).fit().into(holder.img, new Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Picasso onSuccess");
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                Log.d(TAG, "Picasso onError");
                holder.progress.setVisibility(View.GONE);
            }
        });

        // Click
        if (carroOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carroOnClickListener.onClickCarro(holder.itemView, position); // A variável position é final
                }
            });
        }
    }

    public interface CarroOnClickListener {
        public void onClickCarro(View view, int idx);
    }

    // ViewHolder com as views
    public static class CarrosViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        ImageView img;
        ProgressBar progress;

        public CarrosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            img = (ImageView) view.findViewById(R.id.img_adapter);
            progress = (ProgressBar) view.findViewById(R.id.progressbar_cardviewadapter);
        }
    }
}
