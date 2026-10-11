import { toast } from "@/components/ui/toast";

const genericErrorDescription = "Something went wrong. Try again.";

export function toastGenericError() {
  toastError(genericErrorDescription);
}

export function toastError(description?: string) {
  toast.add({
    title: "Error",
    description: description || genericErrorDescription,
    type: "error",
  });
}
