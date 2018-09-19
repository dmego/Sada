package main

import (
	"fmt"

	"encoding/json"
	

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}

type Asset struct {
	Id string `json:"id"`
	Name string `json:"name"`
	KeyInfo string `json:"keyInfo"`
	Owner string `json:"owner"`
	AssetMd5 string `json:"assetMd5"`
	AssetType string `json:"assetType"`
	FileName string `json:"fileName"`
	FilePath string `json:"filePath"`
}

// Init initializes the chaincode state
func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)

}

// Invoke makes payment of X units from A to B
func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("########### example_cc Invoke ###########")
	function, args := stub.GetFunctionAndParameters()

	if function != "invoke" {
		return shim.Error("Unknown function call")
	}

	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting at least 2")
	}

	if args[0] == "delete" {
		// Deletes an entity from its state
		return t.delete(stub, args)
	}

	if args[0] == "query" {
		// queries an entity state
		return t.query(stub, args)
	}
	if args[0] == "save" {
		// Deletes an entity from its state
		return t.save(stub, args)
	}
	if args[0] == "update" {
		// Deletes an entity from its state
		return t.update(stub, args)
	}
	return shim.Error("Unknown action, check the first argument, must be one of 'delete', 'query', or 'move'")
}

func (t *SimpleChaincode) save(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// must be an invoke
	var err error
	fmt.Println("starting save_asset")

	if len(args) != 9 {
		return shim.Error("Incorrect number of arguments. Expecting 9. key of the variable and value to set")
	}

	

	var asset Asset
	asset.Id = args[1]
	asset.Name = args[2]
	asset.KeyInfo = args[3]
	asset.Owner = args[4]
	asset.AssetMd5 = args[5]
	asset.AssetType = args[6]
	asset.FileName = args[7]
	asset.FilePath = args[8]

	//check if asset id already exists

	valAsbytes, err := stub.GetState(args[1])           //get the var from ledger
	if err != nil {
		fmt.Println("This asset already exists - " + args[1])
		fmt.Println(valAsbytes)
		return shim.Error("This asset already exists - " + args[1])  
	}
	

	assetAsBytes, _ := json.Marshal(asset)    //convert to array of bytes
	err = stub.PutState(args[1], assetAsBytes)     //store asset with id as key
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Println("- end init_marble")
	return shim.Success(nil)
}


func (t *SimpleChaincode) update(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var err error
	fmt.Println("starting update_asset")

	if len(args) != 9 {
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}

	
	var id = args[1]
	var new_name = args[2]
	var new_keyInfo = args[3]
	var new_owner = args[4]
	var new_md5 = args[5]
	var new_type = args[6]
	var new_fileName = args[7]
	var new_filePath = args[8]
	fmt.Println(id + "->" + new_name + " - |" + new_keyInfo + " - |" + new_owner)

	

	// get marble's current state
	assetAsBytes, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Failed to get asset")
	}
	res := Asset{}
	json.Unmarshal(assetAsBytes, &res)           //un stringify it aka JSON.parse()

	// transfer the marble
	res.Name = new_name
	res.KeyInfo = new_keyInfo
	res.Owner = new_owner
	res.AssetMd5 = new_md5
	res.AssetType = new_type
	res.FileName = new_fileName
	res.FilePath = new_filePath
	jsonAsBytes, _ := json.Marshal(res)           //convert to array of bytes
	err = stub.PutState(id, jsonAsBytes)     //rewrite the marble with id as key
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Println("- end set owner")
	return shim.Success(nil)
}

// Deletes an entity from state
func (t *SimpleChaincode) delete(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	A := args[1]

	// Delete the key from the state in ledger
	err := stub.DelState(A)
	if err != nil {
		return shim.Error("Failed to delete state")
	}

	return shim.Success(nil)
}

// Query callback representing the query of a chaincode
func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var A string // Entities
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	A = args[1]

	// Get the state from the ledger
	Avalbytes, err := stub.GetState(A)
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for " + A + "\"}"
		return shim.Error(jsonResp)
	}

	if Avalbytes == nil {
		jsonResp := "{\"Error\":\"Nil amount for " + A + "\"}"
		return shim.Error(jsonResp)
	}

	jsonResp := "{\"Name\":\"" + A + "\",\"Amount\":\"" + string(Avalbytes) + "\"}"
	fmt.Printf("Query Response:%s\n", jsonResp)
	return shim.Success(Avalbytes)
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
