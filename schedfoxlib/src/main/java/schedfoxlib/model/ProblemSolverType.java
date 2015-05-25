/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ProblemSolverType implements Serializable {
    private Integer problemsolverTypeId;
    private String typeName;
    
    public ProblemSolverType() {
        
    }
    
    public ProblemSolverType(Record_Set rst) {
        try {
            this.problemsolverTypeId = rst.getInt("problemsolver_type_id");
        } catch (Exception exe) {}
        try {
            this.typeName = rst.getString("type_name");
        } catch (Exception exe) {}
    }

    /**
     * @return the problemsolverTypeId
     */
    public Integer getProblemsolverTypeId() {
        return problemsolverTypeId;
    }

    /**
     * @param problemsolverTypeId the problemsolverTypeId to set
     */
    public void setProblemsolverTypeId(Integer problemsolverTypeId) {
        this.problemsolverTypeId = problemsolverTypeId;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
