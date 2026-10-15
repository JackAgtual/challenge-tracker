import { toast } from "@/components/ui/toast";
import { components } from "@/types/api";
import { error } from "console";

const genericErrorDescription = "Something went wrong. Try again.";

export function toastError(description?: string) {
  toast.add({
    title: "Error",
    description: description || genericErrorDescription,
    type: "error",
  });
}

export function toastGenericError() {
  toastError(genericErrorDescription);
}

export function toastProblemDetail(
  problemDetail: components["schemas"]["ProblemDetail"]
) {
  toastError(problemDetail.detail);
}
