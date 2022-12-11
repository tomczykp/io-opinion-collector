package pl.lodz.p.it.opinioncollector.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/field")
public class FieldController {

    private FieldManager fieldManager;
    @Autowired
    public FieldController(FieldManager fieldManager){
        this.fieldManager = fieldManager;
    }

    @GetMapping("/{uuid}")
    public Field getField(@PathVariable("uuid") UUID uuid)
    {
        return fieldManager.getField(uuid);
    }
    @PutMapping("/field")
    public Field createField(@PathVariable String name, @PathVariable Class type){
        return fieldManager.createField(name, type);
    }
    @DeleteMapping("/{name}")
    public void deleteCategory(@PathVariable("name") String name){
        fieldManager.deleteField(name);
    }
}
