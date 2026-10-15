import { components } from "@/types/api";

export function getFormErrorMessage(
  problemDetail: components["schemas"]["ProblemDetail"]
) {
  return { message: problemDetail.detail ?? "Something went wrong" };
}
