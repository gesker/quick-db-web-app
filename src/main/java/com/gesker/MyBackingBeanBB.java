package com.gesker;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
@Named
@SessionScoped
public class MyBackingBeanBB implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(MyBackingBeanBB.class.getName());
    @Serial
    private static final long serialVersionUID = 1L;
    @Inject
    FacesContext facesContext;
    @EJB
    MyEJB myEJB;

    private List<MyJPAObject> myJPAObjectList;

    public List<MyJPAObject> getMyJPAObjectList() {
        myJPAObjectList = myEJB.getAllMyJpaObjects();
        return myJPAObjectList;
    }
}
