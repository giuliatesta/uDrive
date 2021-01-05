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
import static it.giuliatesta.udrive.R.drawable.img_direction_backward;
import static it.giuliatesta.udrive.R.drawable.img_direction_forward;
import static it.giuliatesta.udrive.R.drawable.img_direction_left;
import static it.giuliatesta.udrive.R.drawable.img_direction_right;
import static it.giuliatesta.udrive.R.layout.direction_and_vertical_motion_event_view;
import static it.giuliatesta.udrive.R.layout.direction_or_vertical_motion_event_view;
import static it.giuliatesta.udrive.accelerometer.EventType.BOTH;
import static it.giuliatesta.udrive.accelerometer.EventType.DIRECTION_EVENT;

public class CustomAdapter implements ListAdapter {
    private ArrayList<AccelerometerDataEvent> accelerometerEventList;
    private Context context;
    private TextView textFirstRow, textSecondRow;
    private ImageView imageFirstRow, imageSecondRow;


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
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            // Scelgo il tipo di View
            if(rowType == 1) {
                convertView = layoutInflater.inflate(direction_and_vertical_motion_event_view, null);
            } else {
                convertView = layoutInflater.inflate(direction_or_vertical_motion_event_view, null);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            
            textFirstRow = convertView.findViewById(R.id.list_item_text);
            imageFirstRow = convertView.findViewById(R.id.list_item_image);
            imageSecondRow = convertView.findViewById(R.id.list_item_image2);
            textSecondRow = convertView.findViewById(R.id.list_item_text2);

            if (rowType == 0) {
                // Se il tipo di evento è VERTICAL_MOTION_EVENT oppure DIRECTION_EVENT
                chooseTypeOfView(accelerometerEventList.get(position), convertView);
            } else {
                // Se il tipo di evento è BOTH
                listItemSettingsDirectionAndVerticalMotionEvent(accelerometerEventList.get(position), convertView);

            }
        }
        return convertView;
    }

    private void chooseTypeOfView(AccelerometerDataEvent event, View convertView) {
        if(event.getType() == DIRECTION_EVENT) {
            listItemSettingsDirectionEvent(textFirstRow, imageFirstRow, event, convertView);
        } else {
            listItemSettingsVerticalMotionEvent(textFirstRow, imageFirstRow, event, convertView);
        }
    }

    /**
     * Gestisce le impostazioni per la view con una riga e con un evento di tipo VERTICAL_MOTION_EVENT
     * @param text
     * @param image
     * @param event evento da cui ricavare le informazioni da visualizzare
     * @param convertView view della singola riga
     */
    private void listItemSettingsVerticalMotionEvent(TextView text, ImageView image, AccelerometerDataEvent event, View convertView) {
        // Trovo la percentuale da inserire nel testo della listView
        int percentage = event.getVerticalMotionPercentage();
        // Trovo l'immagine da inserire nella listView
        Drawable imageDrawable = context.getResources().getDrawable(R.drawable.img_vertical_motion);
        text.setText(percentage + "%");
        text.setTextColor(BLUE);

        image.setImageDrawable(imageDrawable);
        // Trovo il colore di sfondo da mettere
        int colorFilter = setColor(percentage);
        image.setColorFilter(colorFilter);
    }

    /**
     * Imposto le impostazioni per la view con una riga e con un evento di tipo DIRECTION_EVENT
     * @param event evento da cui ricavare le impostazioni da visualizzare
     * @param convertView view della singola riga
     */
    private void listItemSettingsDirectionEvent(TextView text, ImageView image, AccelerometerDataEvent event, View convertView) {
        // Trovo la percentuale da inserire nel testo della listView
        int percentage = event.getDirectionPercentage();
        // Trovo l'immagine da inserire nella listView
        int imageResource = getImageResourceFromDirection(event.getDirection());
        Drawable imageDrawable = context.getResources().getDrawable(imageResource);

        text.setText(percentage + "%");
        text.setTextColor(BLUE);

        image.setImageDrawable(imageDrawable);
        // Trovo il colore di sfondo da mettere
        int colorFilter = setColor(percentage);
        image.setColorFilter(colorFilter);
    }


    /**
     * Imposta le impostazioni per la view con due righe con un evento di tipo BOTH
     * @param event evento da cui recuperare le informazioni da visualizzare
     * @param convertView view
     */
    private void listItemSettingsDirectionAndVerticalMotionEvent(AccelerometerDataEvent event, View convertView) {
        // Per la prima riga uso la direzione
        listItemSettingsDirectionEvent(textFirstRow, imageFirstRow, event, convertView);

        //Per la seconda riga uso il movimento verticale
        listItemSettingsVerticalMotionEvent(textSecondRow, imageSecondRow, event, convertView);
    }


    /**
     * Sceglie il colore di sfondo in base al punteggio ottenuto
     * @param percentage punteggio
     * @return colore scelto
     */
    private int setColor(int percentage) {
        if(percentage >= 75) {
            // Verde
             return Color.parseColor("#04c717");
        } else if (percentage <= 25) {
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
                return img_direction_forward;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(accelerometerEventList.get(position).getType() == BOTH) {
            return 1;
        } else {
            // Se è VERTICAL_MOTION_EVENT oppure DIRECTION_EVENT
            return 0;
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
