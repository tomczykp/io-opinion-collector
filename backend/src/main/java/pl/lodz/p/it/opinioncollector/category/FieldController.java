package pl.lodz.p.it.opinioncollector.category;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.productManagment.ProductDTO;

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
    @PutMapping("")
    public Field createField(@Valid @RequestBody FieldDTO fieldDTO) throws ClassNotFoundException, NoSuchMethodException {
        return fieldManager.createField(fieldDTO);

    }
    @DeleteMapping("/{name}")
    public void deleteCategory(@PathVariable("name") String name){
        fieldManager.deleteField(name);
    }
}
