package websocket.avito.server;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import websocket.avito.metier.Message;


public class EncoderDecoder implements Decoder.Text<Message>,Encoder.Text<Message>{

    @Override
    public Message decode(String message) throws DecodeException {
        System.out.println("Decodage...");
        Reader reader = new StringReader(message);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject objet = jsonReader.readObject();
        Message msg = new Message();
        msg.setType(objet.getJsonString("type").getString());
        msg.setContenu(objet.getJsonString("contenu").getString());
        msg.setEmetteur(objet.getJsonString("emetteur").getString());
        msg.setRecepteur(objet.getJsonString("recepteur").getString());
        msg.setTimestamp(new Date().getTime());
        return msg;
    }

    @Override
    public boolean willDecode(String s) {
        System.out.println("On decode : "+s);
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
        System.out.println("Initialisation...");
    }

    @Override
    public void destroy() {
        System.out.println("Desctruction...");
    }

    @Override
    public String encode(Message object) throws EncodeException {
        StringWriter sw = new StringWriter();
        JsonGenerator generateur = Json.createGenerator(sw);
        generateur.writeStartObject();
        generateur.write("type",object.getType());
        generateur.write("contenu",object.getContenu());
        generateur.write("timestamp",object.getTimestamp());
        generateur.write("emetteur",object.getEmetteur());
        generateur.write("recepteur",object.getRecepteur());
        generateur.writeEnd();
        generateur.flush();
        String chaine = sw.toString();
        System.out.println("Encodage de "+chaine);
        return chaine;
    }
    
}
