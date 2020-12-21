package it.giuliatesta.udrive;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;
import it.giuliatesta.udrive.accelerometer.Direction;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;
import static it.giuliatesta.udrive.R.drawable.img_direction_backward;
import static it.giuliatesta.udrive.R.drawable.img_direction_forward;
import static it.giuliatesta.udrive.R.drawable.img_direction_left;
import static it.giuliatesta.udrive.R.drawable.img_direction_right;
import static it.giuliatesta.udrive.R.id.img_diction_forward;
import static it.giuliatesta.udrive.R.layout.single_item_with_vertical_motion;
import static it.giuliatesta.udrive.R.layout.single_item_without_vertical_motion;
import static it.giuliatesta.udrive.accelerometer.VerticalMotion.NONE;

public class CustomAdapter implements ListAdapter {
    private ArrayList<AccelerometerDataEvent> accelerometerEventList;
    private Context context;
    private TextView listItemText, listItemText2;
    private ImageView listItemImage, listItemImage2;


    public CustomAdapter(Context context, ArrayList<AccelerometerDataEvent> list) {
        this.accelerometerEventList = list;
        this.context = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return accelerometerEventList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);
        System.out.println("rowType = " + rowType);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(single_item_without_vertical_motion, null);
            listItemSettingsWithoutVerticalMotion(accelerometerEventList.get(position), convertView);

            if(rowType == 1) {
                convertView = layoutInflater.inflate(single_item_with_vertical_motion, null);
            } else {
                convertView = layoutInflater.inflate(single_item_without_vertical_motion, null);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            listItemSettingsWithoutVerticalMotion(accelerometerEventList.get(position), convertView);

            if (rowType == 1) {
                listItemSettingsWithVerticalMotion(accelerometerEventList.get(position), convertView);
            }
        }
        return convertView;
    }

    private void listItemSettingsWithVerticalMotion(AccelerometerDataEvent event, View convertView) {
        // Impostazioni per la seconda riga (Moviemento verticale)
        listItemImage2 = convertView.findViewById(R.id.list_item_image2);
        listItemText2 = convertView.findViewById(R.id.list_item_text2);

        int percentage = event.getVerticalMotionPercentage();
        listItemText2.setText(percentage + "%");
        listItemText2.setTextColor(BLUE);
        Drawable imageDrawable = context.getResources().getDrawable(R.drawable.img_vertical_motion);
        listItemImage2.setImageDrawable(imageDrawable);

        int colorFilter = setColor(percentage);
        listItemImage2.setColorFilter(colorFilter);
    }

    /**
     * Imposto le impostazioni per la singola riga della listView
     * @param event evento da cui ricavare le impostazioni da visualizzare
     * @param convertView view della singola riga
     */
    private void listItemSettingsWithoutVerticalMotion(AccelerometerDataEvent event, View convertView) {
        listItemText = convertView.findViewById(R.id.list_item_text);
        listItemImage = convertView.findViewById(R.id.list_item_image);

        // Trovo la percentuale da inserire nel testo della listView
        int percentage = event.getDirectionPercentage();
        listItemText.setText(percentage + "%");

        // Trovo l'immagine da inserire nella listView
        int imageResource = getImageResourceFromDirection(event.getDirection());
        Drawable imageDrawable = context.getResources().getDrawable(imageResource);
        listItemImage.setImageDrawable(imageDrawable);
        listItemImage.setColorFilter(WHITE);

        // Trovo il colore di sfondo da mettere
        int colorFilter = setColor(percentage);
        listItemImage.setColorFilter(colorFilter);
        listItemText.setTextColor(Color.BLUE);
    }

    /**
     * Sceglie il colore di sfondo in base al punteggio ottenuto
     * @param percentage punteggio
     * @return colore scelto
     */
    private int setColor(int percentage) {
        if(percentage > 75) {
            // Verde
             return Color.parseColor("#04c717");
        } else if (percentage < 25) {
            // Rosso
            return Color.parseColor("#ff0000");
        } else {
            // Giallo
            return Color.parseColor("#ffbf00");
        }
    }

    /**
     * In base alla direzione dell'evento capisce quale immagine mostrare
     * @param direction direzione dell'evento
     * @return immagine da inserire nella listView
     */
    private int getImageResourceFromDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                // Se la direzione è sinistra, mostrami la freccia verso sinistra
                return img_direction_left;
            case RIGHT:
                return img_direction_right;
            case FORWARD:
                return img_direction_forward;
            case BACKWARD:
                return img_direction_backward;
            default:
                // Non dovrebbe mai finire qui.
                return img_diction_forward;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(accelerometerEventList.get(position).getVerticalMotion() == NONE) {
            // Se non c'è movimento verticale
            return 0;
        } else {
            // Se c'è movimento verticale
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
